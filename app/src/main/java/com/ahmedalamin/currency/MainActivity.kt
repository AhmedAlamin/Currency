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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope

import com.ahmedalamin.currency.ui.theme.CurrencyTheme
import com.ahmedalamin.domain.entity.ApiRates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: CurrencyViewModel by viewModels()
    private var isLoading by mutableStateOf(true) // Set initial state to loading

    var data: ApiRates? = null
    private var allResponseData: ApiRates? = null
    private var listOfCurrencies: List<String>? = null





    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.getRates()

//        lifecycleScope.launch {
//            try {
//                viewModel.currenciesRates.collect { it ->
//
////                    listOfCurrencies = it?.rates?.map { it.key }
////                    allResponseData = it
//////                    allResponseData?.let { it1 -> updateUIWithData(it1) }
////                    allResponseData?.let { it1 -> updateUIWithData(it1) }
//
//                    if (it != null) {
//                        updateUIWithData(it)
//                    }
//
//
//                }
//            } catch (e: Exception) {
//                // Handle error
//            } finally {
//                isLoading = false // Update loading state when data is fetched or an error occurs
//            }
//        }


        setContent {
            val currenciesRates by viewModel.currenciesRates.collectAsState()
            CurrencyScreen(currenciesRates)
        }
    }

    private fun updateUIWithData(data: ApiRates) {
        isLoading = false // Ensure loading state is set to false


//        allResponseData = data // Update response with fetched items
        listOfCurrencies = data.rates?.map { it.key } // Update selectedCurrency1 with fetched selection
    }
}




fun calculateCurrencyAmount(
    ratesResponse: ApiRates,
    baseCurrency: Double,
    targetCurrency: String,
    amount: Double,
): Double? {
    val rates = ratesResponse

    if (baseCurrency != null) {
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(currenciesRates: ApiRates?) {
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

                    var amount by remember {
                        mutableStateOf(currenciesRates?.rates?.get(selectedCurrency1))
                    }
                    var result by remember {
                        mutableStateOf(currenciesRates?.rates?.get(selectedCurrency2))
                    }


                    if (listOfCurrencies.isEmpty()) {
                        // Show loading indicator (e.g., ProgressBar)
                        CircularProgressIndicator(modifier = Modifier.wrapContentSize())
                    } else {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
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
                                                    onItemSelected = { selectedCurrency1 = it },
                                                    modifier = Modifier.wrapContentWidth()
                                                )
                                            }
                                        }
                                    }


                                    Spacer(modifier = Modifier.size(16.dp))



                                    TextField(
                                        value = amount.toString(), onValueChange = {
                                         amount = it.toDouble()
                                            result = amount?.let { it1 ->
                                                currenciesRates?.rates?.get(selectedCurrency2)?.let { it2 ->
                                                    calculateCurrencyAmount(
                                                        currenciesRates,
                                                        it2,selectedCurrency2!!,
                                                        it1.toDouble())
                                                }
                                            }!!

                                        }, modifier = Modifier
                                            .size(100.dp)
                                            .padding(16.dp)
                                    )
                                }


                                IconTextButton(text = "Swich", icon = Icons.Default.Info, onClick = {})


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
                                                    onItemSelected = { selectedCurrency2 = it },
                                                    modifier = Modifier.wrapContentWidth()
                                                )
                                            }
                                        }
                                    }


                                    Spacer(modifier = Modifier.size(16.dp))


                                    Text(
                                        text = result.toString() , Modifier
                                            .background(Color.Magenta)
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

                                })

                        }
                    }


                }
            }
                    }







