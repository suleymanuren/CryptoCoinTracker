package com.suleymanuren.cryptotracker.adapter

import android.graphics.Color
import android.net.Uri
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suleymanuren.cryptotracker.R
import com.suleymanuren.cryptotracker.databinding.RowLayoutBinding
import com.suleymanuren.cryptotracker.model.CryptoModelItem
import com.suleymanuren.cryptotracker.model.CryptoModelX
import kotlin.math.max

class RecyclerViewAdapter(private val cryptoList: ArrayList<CryptoModelItem>,private val listener : Listener) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    private val colors : Array<String> = arrayOf("#13bd27","#29c1e1","#b129e1","#d3df13","#f6bd0c","#a1fb93","#0d9de3","#ffe48f")


    class RowHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RowHolder(binding)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClick(cryptoList[position])
        }
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))
        holder.binding.cryptoName.text = cryptoList[position].asset_id
        holder.binding.cryptoLongName.text = cryptoList[position].name
        holder.binding.cryptoPrice.text = String.format("%.4f", cryptoList[position].price_usd) + "$"
        holder.binding.dailyVolumeText.text = "Hourly Volume"


        if(cryptoList[position].volume_1hrs_usd!! >= 1000000000)
        {
            holder.binding.dailyVolume.filters = arrayOf(InputFilter.LengthFilter(10))
            holder.binding.dailyVolume.text = "+" +  "$" + String.format("%.0f", cryptoList[position].volume_1hrs_usd)
        }
        else{
            holder.binding.dailyVolume.text =  "$" + String.format("%.0f", cryptoList[position].volume_1hrs_usd)
        }


    }

    override fun getItemCount(): Int {
        return cryptoList.count()
    }

    interface Listener {
        fun onItemClick(cryptoModelItem: CryptoModelItem)
    }
}