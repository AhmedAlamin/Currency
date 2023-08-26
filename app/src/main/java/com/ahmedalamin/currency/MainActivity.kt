package com.ahmedalamin.currency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController

import com.ahmedalamin.currency.ui.theme.CurrencyTheme
import com.ahmedalamin.domain.entity.ApiRates
import com.ahmedalamin.domain.entity.History
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: CurrencyViewModel by viewModels()
    private var isLoading by mutableStateOf(true) // Set initial state to loading


    private var allResponseData: ApiRates? = null
    private var listOfCurrencies: List<String>? = null




    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var sharedPreferencesManager = SharedPreferencesManager(this)


        viewModel.getRates()

        setContent {
            val currenciesRates by viewModel.currenciesRates.collectAsState()
//            CurrencyScreen(currenciesRates)
             Nav(currenciesRates = currenciesRates,sharedPreferencesManager)
        }
    }

    private fun updateUIWithData(data: ApiRates) {
        isLoading = false // Ensure loading state is set to false


//        allResponseData = data // Update response with fetched items
        listOfCurrencies =
            data.rates?.map { it.key } // Update selectedCurrency1 with fetched selection
    }
}


fun calculateCurrencyAmount(
    ratesResponse: ApiRates,
    baseCurrency: Double,
    targetCurrency: String,
    amount: Double,
): Double? {
    val rates = ratesResponse

    if(baseCurrency != null) {
        return amount * baseCurrency
    } else {
        return null // Target currency not found in rates
    }
}


@Composable
fun DropdownMenuComponent(
    items: List<String?>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }


    Row(modifier = modifier) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = selectedItem, fontSize = 18.sp)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 5.dp) // Adjust the y offset as needed

        ) {
            items.forEach { item ->
                DropdownMenuItem(text = {
                    if (item != null) {
                        Text(text = item)
                    }
                },
                    onClick = {
                        if (item != null) {
                            onItemSelected(item)
                        }
                        expanded = false
                    })

            }

        }
    }
}


@Composable
fun IconTextButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
            Text(text = text)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CurrencyScreen(currenciesRates: ApiRates?,navController: NavController,sharedPreferencesManager: SharedPreferencesManager) {
    val listOfCurrencies = currenciesRates?.rates?.keys?.toList() ?: emptyList()
    val listOfValuesOfCorrencies = currenciesRates?.rates?.values?.toList() ?: emptyList()



    var selectedCurrency1 by remember { mutableStateOf(listOfCurrencies.getOrNull(0)) }
    var selectedCurrency2 by remember { mutableStateOf(listOfCurrencies.getOrNull(0)) }



    CurrencyTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var result by remember {
                mutableStateOf(1.0)
            }

            var amount by remember {
                mutableStateOf(1.0)
            }




            if (listOfCurrencies.isEmpty()) {
                // Show loading indicator (e.g., ProgressBar)
                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
            } else {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {


                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    ) {

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "FROM")

                            Spacer(modifier = Modifier.size(16.dp))

                            if (listOfCurrencies.isEmpty()) {
                                // Show loading indicator (e.g., ProgressBar)
                                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                            } else {

                                listOfCurrencies.let { it1 ->
                                    selectedCurrency1?.let {
                                        DropdownMenuComponent(
                                            items = it1,
                                            selectedItem = it,
                                            onItemSelected = {
                                                selectedCurrency1 = it

                                                result = convertCurrency(
                                                    amount,
                                                    selectedCurrency1!!,
                                                    selectedCurrency2!!,
                                                    currenciesRates!!
                                                )!!
                                            },
                                            modifier = Modifier.wrapContentWidth()
                                        )
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.size(16.dp))

                            var text by remember { mutableStateOf("") }

                            val keyboardController = LocalSoftwareKeyboardController.current



                            var listHistory by remember {
                                mutableStateOf<MutableList<History>>(mutableListOf())
                            }



                            TextField(
                                value = amount.toString(),
                                onValueChange = {
                                    amount = it.toDouble()


                                },
                                keyboardActions = KeyboardActions(onDone ={
                                    result = convertCurrency(
                                        amount,
                                        selectedCurrency1!!,
                                        selectedCurrency2!!,
                                        currenciesRates!!
                                    )!!


                                    val newItem = History(currenciesRates.date,amount,result,selectedCurrency1!!,selectedCurrency2!!)

                                    // Update the historyListState with the new item added
                                    listHistory.add(newItem)


                                    sharedPreferencesManager.saveHistoryList("history",listHistory)


                                } ),

                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                                ,modifier = Modifier
                                    .padding(16.dp)
                                    .width(100.dp)
                                    .padding(16.dp)

                            )
                        }


                        IconTextButton(text = "Swich", icon = Icons.Default.Info, onClick = {

                            var temp = selectedCurrency1
                            selectedCurrency1 = selectedCurrency2
                            selectedCurrency2 = temp

                            result = convertCurrency(
                                amount, selectedCurrency1!!, selectedCurrency2!!, currenciesRates!!
                            )!!

                        })


                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "TO")

                            Spacer(modifier = Modifier.size(16.dp))


                            if (listOfCurrencies.isEmpty()) {
                                // Show loading indicator (e.g., ProgressBar)
                                CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                            } else {

                                listOfCurrencies.let { it2 ->
                                    selectedCurrency2?.let {
                                        DropdownMenuComponent(
                                            items = it2,
                                            selectedItem = it,
                                            onItemSelected = {
                                                selectedCurrency2 = it
                                                result = convertCurrency(
                                                    amount,
                                                    selectedCurrency1!!,
                                                    selectedCurrency2!!,
                                                    currenciesRates!!
                                                )!!
                                            },
                                            modifier = Modifier.wrapContentWidth()
                                        )
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.size(16.dp))


                            Text(
                                text = roundDouble(result, 4).toString(),
                                Modifier
                                    .background(Color.Gray)
                                    .size(100.dp)
                                    .padding(16.dp)
                            )

                        }
                    }
                }



                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {

                    IconTextButton(
                        text = "Details",
                        icon = Icons.Default.PlayArrow,
                        onClick = {
                        navController.navigate("detailScreen")
                        })

                }
            }


        }
    }
}


fun convertCurrency(
    amount: Double,
    fromCurrency: String,
    toCurrency: String,
    exchangeRateData: ApiRates,
): Double? {
    val fromRate = exchangeRateData.rates[fromCurrency]
    val toRate = exchangeRateData.rates[toCurrency]

    if (fromRate != null && toRate != null) {
        val baseAmount = amount / fromRate
        return baseAmount * toRate
    }

    return null // Handle missing exchange rates
}

fun roundDouble(number: Double, digits: Int): Double {
    return BigDecimal(number).setScale(digits, RoundingMode.HALF_UP).toDouble()
}
