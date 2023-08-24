package com.ahmedalamin.currency.di

import com.ahmedalamin.data.remote.ApiService
import com.ahmedalamin.data.repo.CurrencyRepoImpl
import com.ahmedalamin.domain.repo.CurrencyRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule{

    @Provides
    fun provideRepo(apiService: ApiService):CurrencyRepo{
       return CurrencyRepoImpl(apiService)
    }

}