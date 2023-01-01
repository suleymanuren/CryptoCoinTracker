package com.suleymanuren.cryptotracker.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suleymanuren.cryptotracker.adapter.RecyclerViewAdapter
import com.suleymanuren.cryptotracker.databinding.ActivityMainBinding
import com.suleymanuren.cryptotracker.model.CryptoModelItem
import com.suleymanuren.cryptotracker.service.ApiService
import com.suleymanuren.cryptotracker.service.RequestInterceptor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() , RecyclerViewAdapter.Listener {
    private val BASE_URL = "https://rest.coinapi.io/v1/"
    private lateinit var binding: ActivityMainBinding

    private lateinit var cryptoModels : ArrayList<CryptoModelItem>
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private  var recyclerView: RecyclerView?= null

    //Disposable
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        compositeDisposable = CompositeDisposable()

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        loadData()

    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getHttpClient())
            .build().create(ApiService::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse)
        )

    }

    private fun getHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(RequestInterceptor())
        return httpClient.build()
    }

    private fun handleResponse(cryptoList: List<CryptoModelItem>){
        cryptoModels = ArrayList(cryptoList)
       /* cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }*/
        val data: List<CryptoModelItem> = cryptoModels
        val filteredData = data.filter { it.type_is_crypto == 1 && it.price_usd != null && it.price_usd > 0.0}
        recyclerViewAdapter = RecyclerViewAdapter(filteredData as ArrayList<CryptoModelItem>,this@MainActivity)
        binding.recyclerView.adapter = recyclerViewAdapter

    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
    override fun onItemClick(cryptoModelItem: CryptoModelItem) {
       Toast.makeText(this,"Basılan crypto ${cryptoModelItem.name}",Toast.LENGTH_SHORT).show()
    }
}


