package com.suleymanuren.cryptotracker.service

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val apiKeyRequest = originalRequest
            .newBuilder()
            .header("X-CoinAPI-Key", "BEED0FFF-DA21-454C-B1AC-CD08E9D13DC5")
            .build()
        return chain.proceed(apiKeyRequest)
    }
}