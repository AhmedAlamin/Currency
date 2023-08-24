package com.ahmedalamin.domain.repo

import com.ahmedalamin.domain.entity.ApiRates

interface CurrencyRepo {

    fun getRatesFromRemote():ApiRates


}