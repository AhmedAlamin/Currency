package com.ahmedalamin.data.remote

import com.ahmedalamin.domain.entity.ApiRates
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val ACCESS_KEY = "cd795b5186359ba9c46afce1b2b3d802"
    }
    @GET("latest")
  suspend fun getRates(@Query("access_key") accessKey: String= ACCESS_KEY):ApiRates
}