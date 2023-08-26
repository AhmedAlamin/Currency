package com.ahmedalamin.currency

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmedalamin.domain.entity.ApiRates

@Composable
fun Nav(currenciesRates: ApiRates?,sharedPreferencesManager: SharedPreferencesManager) {

    val navController = rememberNavController()

    
    NavHost(navController = navController, startDestination = "mainScreen") {

        composable(route = "detailScreen"){
                DetailsScreen(sharedPreferencesManager)
        }

        composable(route = "mainScreen"){
                CurrencyScreen(currenciesRates,navController,sharedPreferencesManager)
        }


    }

}