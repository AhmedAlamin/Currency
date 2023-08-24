package com.ahmedalamin.data.remote

import com.ahmedalamin.domain.entity.ApiRates
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val ACCESS_KEY = "f52cd5160455f2d748bcc1b2678a1d9a"
    }
    @GET("latest")
  suspend fun getRates(@Query("access_key") accessKey: String= ACCESS_KEY):ApiRates
}