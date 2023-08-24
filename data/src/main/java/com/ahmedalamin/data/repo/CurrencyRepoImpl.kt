package com.ahmedalamin.data.repo

import com.ahmedalamin.data.remote.ApiService
import com.ahmedalamin.domain.entity.ApiRates
import com.ahmedalamin.domain.repo.CurrencyRepo

class CurrencyRepoImpl (private val apiService: ApiService) : CurrencyRepo{
    override suspend fun getRatesFromRemote(): ApiRates = apiService.getRates()


}