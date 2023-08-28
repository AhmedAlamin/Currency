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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getRatesUseCase: GetRates,
) : ViewModel() {

    // Define sealed class for different loading states
    sealed class CurrencyState {
        object Loading : CurrencyState()
        data class Success(val data: ApiRates) : CurrencyState()
        data class Error(val message: String) : CurrencyState()

    }


    private fun handleApiError(errorCode: Int): String {
        return when (errorCode) {
            101 -> "No API Key specified or invalid API Key"
            102 -> "Account is inactive"
            103 -> "Requested API endpoint does not exist"
            104 -> "Maximum allowed monthly API requests reached"
            105 -> "Current subscription plan does not support this API endpoint"
            106 -> "The current request did not return any results"
            201 -> "Invalid base currency entered"
            202 -> "One or more invalid symbols specified"
            301 -> "No date specified"
            302 -> "Invalid date specified"
            403 -> "No or invalid amount specified"
            501 -> "No or invalid timeframe specified"
            502 -> "No or invalid start date specified"
            503 -> "No or invalid end date specified"
            504 -> "Invalid timeframe specified"
            505 -> "Specified timeframe is too long, exceeding 365 days"
            else -> "An error occurred"
        }
    }


    // Use MutableStateFlow to hold the current state
    private val _currencyState: MutableStateFlow<CurrencyState> = MutableStateFlow(CurrencyState.Loading)

    // Expose the currency state as a StateFlow
    val currencyState: StateFlow<CurrencyState> = _currencyState

    var rateValues: List<String>? = null

    init {
        // Automatically fetch rates when the ViewModel is created
        getRates()
    }

    private fun getRates() {
        viewModelScope.launch {
            try {
                // Fetch rates using the use case
                val rates = getRatesUseCase()

                // Update the state to Success with fetched data
                _currencyState.value = CurrencyState.Success(rates)

                rateValues = rates.rates.map { it.toString() }

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> "Network error occurred"
                    is HttpException -> {
                        val errorCode = e.code()
                        handleApiError(errorCode)
                    }
                    else -> "An error occurred"
                }
                _currencyState.value = CurrencyState.Error(errorMessage)
            }
        }
    }
}
