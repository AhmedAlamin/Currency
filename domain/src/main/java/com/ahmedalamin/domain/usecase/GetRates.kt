package com.ahmedalamin.domain.usecase

import com.ahmedalamin.domain.repo.CurrencyRepo

class GetRates(private val currencyRepo: CurrencyRepo) {

  suspend operator fun invoke() = currencyRepo.getRatesFromRemote()



}