package com.ahmedalamin.domain.entity

data class ApiRates(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)