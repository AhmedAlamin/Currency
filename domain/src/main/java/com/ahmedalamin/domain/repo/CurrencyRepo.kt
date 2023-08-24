package com.ahmedalamin.domain.repo

import com.ahmedalamin.domain.entity.ApiRates

interface CurrencyRepo {

   suspend fun getRatesFromRemote():ApiRates





}