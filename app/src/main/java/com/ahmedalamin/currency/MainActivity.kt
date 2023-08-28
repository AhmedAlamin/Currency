package com.ahmedalamin.currency

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.ahmedalamin.currency.ui.theme.CurrencyTheme
import com.ahmedalamin.domain.entity.ApiRates
import com.ahmedalamin.domain.entity.History
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity:ComponentActivity() {

    private val viewModel: CurrencyViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferencesManager = SharedPreferencesManager(this)


        viewModel.getRates()

        setContent {

                val currenciesRates by viewModel.currenciesRates.collectAsState()

                Nav(currenciesRates = currenciesRates, sharedPreferencesManager)






        }
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
fun CurrencyScreen(
    currenciesRates: ApiRates?,
    navController: NavController,
    sharedPreferencesManager: SharedPreferencesManager,
) {
    var listOfCurrencies by remember { mutableStateOf<List<String>>(emptyList()) }

//    // Fetch the list of currencies from API
//    LaunchedEffect(true) {
////        listOfCurrencies = fetchListOfCurrenciesFromApi()
//        listOfCurrencies = currenciesRates?.rates?.keys?.toList() ?: emptyList()
//    }

    // Simulate fetching currenciesRates from API
    LaunchedEffect(true) {
        // Fetch currenciesRates from API (Replace with actual API call)
         // Implement this

        if (currenciesRates != null) {
//            currenciesRates = fetchedRates
            listOfCurrencies = currenciesRates.rates.keys.toList() ?: emptyList()
        }
    }







    Log.d("vvvv",listOfCurrencies.toString())



    var selectedCurrency1 by remember { mutableStateOf(currenciesRates?.rates?.keys?.first()) }
    var selectedCurrency2 by remember { mutableStateOf(currenciesRates?.rates?.keys?.first()) }

    var amount by remember {
        mutableStateOf(1.0)
    }
    var amount2 by remember {
        mutableStateOf(1.0)
    }

    var listHistory by remember {
        mutableStateOf<MutableList<History>>(mutableListOf())
    }

    val keyboardController = LocalSoftwareKeyboardController.current


    CurrencyTheme {

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Content for the first row (1/3 of the total height)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .weight(2.0f, true)
                    ) {

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1.0f,true)
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

                                                amount2 = convertCurrency(
                                                    amount,
                                                    selectedCurrency1!!,
                                                    selectedCurrency2!!,
                                                    currenciesRates!!
                                                )?: 0.0
                                            },
                                            modifier = Modifier.wrapContentWidth()
                                        )
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.size(16.dp))



                        }


                        IconTextButton(text = "Switch", icon = Icons.Default.Info, onClick = {

                            var temp = selectedCurrency1
                            selectedCurrency1 = selectedCurrency2
                            selectedCurrency2 = temp

                            var temp2 = amount
                            amount = amount2
                            amount2 = temp2


                            amount2 = convertCurrency(
                                amount,
                                selectedCurrency1!!,
                                selectedCurrency2!!,
                                currenciesRates!!
                            )?:0.0
                        },
                        )


                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1.0f,true)
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
                                                amount = convertCurrency(
                                                    amount2,
                                                    selectedCurrency1!!,
                                                    selectedCurrency2!!,
                                                    currenciesRates!!
                                                )?:0.0
                                            },
                                            modifier = Modifier.wrapContentWidth()
                                        )
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.size(16.dp))




                        }

                    }


                }

                Row( verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    ) {
                        TextField(
                            value = amount.toString(),
                            onValueChange = {
                                amount = it.toDouble()

                                amount2 = convertCurrency(
                                    amount,
                                    selectedCurrency1!!,
                                    selectedCurrency2!!,
                                    currenciesRates!!
                                )?:0.0
                            },
                            keyboardActions = KeyboardActions(onDone = {
                                amount2 = convertCurrency(
                                    amount,
                                    selectedCurrency1!!,
                                    selectedCurrency2!!,
                                    currenciesRates!!
                                )!!


                                // Retrieve the existing history list
                                listHistory = sharedPreferencesManager.getHistoryList("history")
                                    .toMutableList()

                                // Append a new item to the list
                                val newItem = History(
                                    currenciesRates.date,
                                    amount,
                                    amount2,
                                    selectedCurrency1!!,
                                    selectedCurrency2!!
                                )
                                listHistory.add(newItem)

                                sharedPreferencesManager.saveHistoryList("history", listHistory)


                                keyboardController?.hide()
                            }),

                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ), modifier = Modifier
                                .padding(16.dp)
                                .weight(1.0f)
                                .padding(16.dp)
                        )



                        TextField(
                            value = amount2.toString(),
                            onValueChange = {
                                amount2 = it.toDouble()

                                amount = convertCurrency(
                                    amount2,
                                    selectedCurrency1!!,
                                    selectedCurrency2!!,
                                    currenciesRates!!
                                )?:0.0
                            },
                            keyboardActions = KeyboardActions(onDone = {
                                amount = convertCurrency(
                                    amount2,
                                    selectedCurrency1!!,
                                    selectedCurrency2!!,
                                    currenciesRates!!
                                )!!

                                // Retrieve the existing history list
                                listHistory = sharedPreferencesManager.getHistoryList("history")
                                    .toMutableList()

                                // Append a new item to the list
                                val newItem = History(
                                    currenciesRates.date,
                                    amount,
                                    amount2,
                                    selectedCurrency1!!,
                                    selectedCurrency2!!
                                )
                                listHistory.add(newItem)

                                sharedPreferencesManager.saveHistoryList("history", listHistory)

                                keyboardController?.hide()
                            }),

                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ), modifier = Modifier
                                .padding(16.dp)
                                .weight(1.0f)
                                .padding(16.dp)
                        )


                    }

                }

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                ) {
                    // Content for the third row (1/4 of the total height)

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    ) {

                        //
                        IconTextButton(
                            text = "Details",
                            icon = Icons.Default.PlayArrow,
                            onClick = {
                                navController.navigate("detailScreen")
                            })

                    }
                }
            }





//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                var amount by remember {
//                    mutableStateOf(1.0)
//                }
//                var amount2 by remember {
//                    mutableStateOf(1.0)
//                }
//
//                var listHistory by remember {
//                    mutableStateOf<MutableList<History>>(mutableListOf())
//                }
//
//
//                if (listOfCurrencies.isEmpty()) {
//                    // Show loading indicator (e.g., ProgressBar)
//                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
//                } else {
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//
//
//                        Row(
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .padding(top = 16.dp)
//                                .weight(2.0f, true)
//                        ) {
//
//                            Column(
//                                verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                modifier = Modifier.weight(1.0f,true)
//                            ) {
//                                Text(text = "FROM")
//
//                                Spacer(modifier = Modifier.size(16.dp))
//
//                                if (listOfCurrencies.isEmpty()) {
//                                    // Show loading indicator (e.g., ProgressBar)
//                                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
//                                } else {
//
//                                    listOfCurrencies.let { it1 ->
//                                        selectedCurrency1?.let {
//                                            DropdownMenuComponent(
//                                                items = it1,
//                                                selectedItem = it,
//                                                onItemSelected = {
//                                                    selectedCurrency1 = it
//
//                                                    amount2 = convertCurrency(
//                                                        amount,
//                                                        selectedCurrency1!!,
//                                                        selectedCurrency2!!,
//                                                        currenciesRates!!
//                                                    )!!
//                                                },
//                                                modifier = Modifier.wrapContentWidth()
//                                            )
//                                        }
//                                    }
//                                }
//
//
//                                Spacer(modifier = Modifier.size(16.dp))
//
//
//
//                            }
//
//
//                            IconTextButton(text = "Swich", icon = Icons.Default.Info, onClick = {
//
//                                var temp = selectedCurrency1
//                                selectedCurrency1 = selectedCurrency2
//                                selectedCurrency2 = temp
//
//                                var temp2 = amount
//                                amount = amount2
//                                amount2 = temp2
//
//
//                                amount2 = convertCurrency(
//                                    amount, selectedCurrency1!!, selectedCurrency2!!, currenciesRates!!
//                                )!!
//
//                            },
//                            )
//
//
//                            Column(
//                                verticalArrangement = Arrangement.Center,
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                modifier = Modifier.weight(1.0f,true)
//                            ) {
//                                Text(text = "TO")
//
//                                Spacer(modifier = Modifier.size(16.dp))
//
//
//                                if (listOfCurrencies.isEmpty()) {
//                                    // Show loading indicator (e.g., ProgressBar)
//                                    CircularProgressIndicator(modifier = Modifier.wrapContentSize())
//                                } else {
//
//                                    listOfCurrencies.let { it2 ->
//                                        selectedCurrency2?.let {
//                                            DropdownMenuComponent(
//                                                items = it2,
//                                                selectedItem = it,
//                                                onItemSelected = {
//                                                    selectedCurrency2 = it
//                                                    amount = convertCurrency(
//                                                        amount2,
//                                                        selectedCurrency1!!,
//                                                        selectedCurrency2!!,
//                                                        currenciesRates!!
//                                                    )!!
//                                                },
//                                                modifier = Modifier.wrapContentWidth()
//                                            )
//                                        }
//                                    }
//                                }
//
//
//                                Spacer(modifier = Modifier.size(16.dp))
//
//
//
//
//                            }
//
//                        }
//
//
//
//                        Row(
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.Top,
//                            modifier = Modifier
//                                .padding(bottom = 16.dp)
//                        ) {
//                            TextField(
//                                value = amount.toString(),
//                                onValueChange = {
//                                    amount = it.toDouble()
//
//                                    amount2 = convertCurrency(
//                                        amount,
//                                        selectedCurrency1!!,
//                                        selectedCurrency2!!,
//                                        currenciesRates!!
//                                    )!!
//                                },
//                                keyboardActions = KeyboardActions(onDone = {
//                                    amount2 = convertCurrency(
//                                        amount,
//                                        selectedCurrency1!!,
//                                        selectedCurrency2!!,
//                                        currenciesRates!!
//                                    )!!
//
//
//                                    // Retrieve the existing history list
//                                    listHistory = sharedPreferencesManager.getHistoryList("history")
//                                        .toMutableList()
//
//                                    // Append a new item to the list
//                                    val newItem = History(
//                                        currenciesRates.date,
//                                        amount,
//                                        amount2,
//                                        selectedCurrency1!!,
//                                        selectedCurrency2!!
//                                    )
//                                    listHistory.add(newItem)
//
//                                    sharedPreferencesManager.saveHistoryList("history", listHistory)
//
//
//                                }),
//
//                                keyboardOptions = KeyboardOptions.Default.copy(
//                                    keyboardType = KeyboardType.Number,
//                                    imeAction = ImeAction.Done
//                                ), modifier = Modifier
//                                    .padding(16.dp)
//                                    .weight(1.0f)
//                                    .padding(16.dp)
//                            )
//
//
//
//                            TextField(
//                                value = amount2.toString(),
//                                onValueChange = {
//                                    amount2 = it.toDouble()
//
//                                    amount = convertCurrency(
//                                        amount2,
//                                        selectedCurrency1!!,
//                                        selectedCurrency2!!,
//                                        currenciesRates!!
//                                    )!!
//                                },
//                                keyboardActions = KeyboardActions(onDone = {
//                                    amount = convertCurrency(
//                                        amount2,
//                                        selectedCurrency1!!,
//                                        selectedCurrency2!!,
//                                        currenciesRates!!
//                                    )!!
//
//                                    // Retrieve the existing history list
//                                    listHistory = sharedPreferencesManager.getHistoryList("history")
//                                        .toMutableList()
//
//                                    // Append a new item to the list
//                                    val newItem = History(
//                                        currenciesRates.date,
//                                        amount,
//                                        amount2,
//                                        selectedCurrency1!!,
//                                        selectedCurrency2!!
//                                    )
//                                    listHistory.add(newItem)
//
//                                    sharedPreferencesManager.saveHistoryList("history", listHistory)
//
//
//                                }),
//
//                                keyboardOptions = KeyboardOptions.Default.copy(
//                                    keyboardType = KeyboardType.Number,
//                                    imeAction = ImeAction.Done
//                                ), modifier = Modifier
//                                    .padding(16.dp)
//                                    .weight(1.0f)
//                                    .padding(16.dp)
//                            )
//
//
//                        }
//
//
//
//                        Row(
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.Top,
//                            modifier = Modifier
//                                .padding(bottom = 16.dp)
//                        ) {
//
//                            //
//                            IconTextButton(
//                                text = "Details",
//                                icon = Icons.Default.PlayArrow,
//                                onClick = {
//                                    navController.navigate("detailScreen")
//                                })
//
//                        }
//                    }
//                }
//
//            }






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
        return roundDouble(baseAmount * toRate, 4)
    }

    return null // Handle missing exchange rates
}

fun roundDouble(number: Double, digits: Int): Double {
    return BigDecimal(number).setScale(digits, RoundingMode.FLOOR).toDouble()
}
