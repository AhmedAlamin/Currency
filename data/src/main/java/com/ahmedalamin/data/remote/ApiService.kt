package com.ahmedalamin.data.remote

import com.ahmedalamin.domain.entity.ApiRates
import retrofit2.http.GET

interface ApiService {

    @GET("latest?access_key=cd795b5186359ba9c46afce1b2b3d802")
   fun getRates():ApiRates
}