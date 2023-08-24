package com.ahmedalamin.domain.entity

data class ApiRates(
    val base: String,
    val date: String,
    val rates: Map<String,Double>,
    val success: Boolean,
    val timestamp: Int
)