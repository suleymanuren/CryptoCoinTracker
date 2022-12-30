package com.suleymanuren.cryptotracker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    /*private fun loadData(){

     ApiClient.getApiService().getData().enqueue(object : Callback<List<CryptoModelItem>>{
         override fun onResponse(
             call: Call<List<CryptoModelItem>>,
             response: Response<List<CryptoModelItem>>,
         ) {
             if (response.isSuccessful){
                 response.body()?.let {
                     cryptoModels = ArrayList(it)

                     cryptoModels?.let {
                         Log.d("deneme",it.size.toString())
                         recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
                         recyclerView?.adapter = recyclerViewAdapter
                     }

                   /*  cryptoModels?.let {
                         recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
                         recyclerView?.adapter = recyclerViewAdapter

                         Log.d("deneme",cryptoModels.size.toString())
                     }*/
                 }
             }
         }

         override fun onFailure(call: Call<List<CryptoModelItem>>, t: Throwable) {
             Log.d("deneme","gelmedi")
         }

     })


    } */
    private fun handleResponse(cryptoList: List<CryptoModelItem>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter

        }
    }
    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }
    override fun onItemClick(cryptoModelItem: CryptoModelItem) {
        Log.d("deneme","basıldı")
    }
}


