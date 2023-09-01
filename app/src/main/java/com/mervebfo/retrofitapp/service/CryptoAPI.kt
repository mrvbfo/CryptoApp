package com.mervebfo.retrofitapp.service

import android.database.Observable
import com.mervebfo.retrofitapp.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {
    @GET("prices?key=<YOUR_KEY>")
    fun getData(): io.reactivex.Observable<List<CryptoModel>>
}