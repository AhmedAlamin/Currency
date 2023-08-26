package com.ahmedalamin.currency

import android.content.Context
import android.content.SharedPreferences
import com.ahmedalamin.domain.entity.ApiRates
import com.ahmedalamin.domain.entity.History
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("history", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveHistory(key: String, history: History) {
        val historyList = getHistoryList(key).toMutableList()
        historyList.add(history)
        saveHistoryList(key, historyList)
    }


    fun getHistoryList(key: String): List<History> {
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<List<History>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveHistoryList(key: String, historyList: List<History>) {
        val json = gson.toJson(historyList)
        sharedPreferences.edit().putString(key, json).apply()
    }
}