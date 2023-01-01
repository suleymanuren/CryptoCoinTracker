package com.suleymanuren.cryptotracker.service

import com.suleymanuren.cryptotracker.model.CryptoModelItem
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiService {
   // @GET("assets?filter_asset_id=BTC;ETH;LTC;BNB")
    @GET("assets?")

    fun getData() : Observable<List<CryptoModelItem>>

}