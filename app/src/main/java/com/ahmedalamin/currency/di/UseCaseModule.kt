package com.ahmedalamin.currency.di

import com.ahmedalamin.data.remote.ApiService
import com.ahmedalamin.data.repo.CurrencyRepoImpl
import com.ahmedalamin.domain.repo.CurrencyRepo
import com.ahmedalamin.domain.usecase.GetRates
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule{

    @Provides
   fun provideUseCase(currencyRepo: CurrencyRepo):GetRates{
       return GetRates(currencyRepo)
   }
}