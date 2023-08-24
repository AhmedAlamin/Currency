package com.ahmedalamin.currency

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope

import com.ahmedalamin.currency.ui.theme.CurrencyTheme
import com.ahmedalamin.domain.entity.ApiRates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CurrencyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.getRates()

        var rates: ApiRates? = null



        lifecycleScope.launch {
            viewModel.currenciesRates.collect {
                rates = it
                Log.e("ccMain", it.toString())

            }
        }

        setContent {
            var response by remember { mutableStateOf(rates) }
            CurrencyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    Greeting(response.toString())
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyTheme {
        Greeting("Android")
    }
}


fun calculateCurrencyAmount(ratesResponse: ApiRates,baseCurrency:Double, targetCurrency: String, amount: Double): Double? {
    val rates = ratesResponse.rates


    if (baseCurrency != null) {
        return amount * baseCurrency
    } else {
        return null // Target currency not found in rates
    }
}


