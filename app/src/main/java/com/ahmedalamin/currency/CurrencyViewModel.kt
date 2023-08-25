package com.ahmedalamin.currency

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedalamin.data.remote.ApiService
import com.ahmedalamin.domain.entity.ApiRates
import com.ahmedalamin.domain.usecase.GetRates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getRatesUseCase: GetRates,
) : ViewModel() {

    private val _currenciesRates : MutableStateFlow<ApiRates?> = MutableStateFlow(null)

    val currenciesRates:StateFlow<ApiRates?> = _currenciesRates
     var rateValues: List<String>? = null

    fun getRates() {


        viewModelScope.launch() {
            try {

            _currenciesRates.value =  getRatesUseCase()

                 rateValues = currenciesRates.value?.rates!!.map { it.toString() }

                Log.e("ccViewModel2",currenciesRates.value.toString())



//            getRatesUseCase().apply {
//                _currenciesRates.value?.rates
//                Log.e("ccViewModel2",currenciesRates.toString())
//
//            }

            }catch (e:Exception){
                Log.e("ccViewModel",e.message.toString())
            }
        }
    }



}