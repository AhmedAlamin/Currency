package com.ahmedalamin.currency


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedalamin.domain.entity.ApiRates
import com.ahmedalamin.domain.entity.History
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DetailsScreen(sharedPreferencesManager: SharedPreferencesManager,currenciesRates: ApiRates?) {


    val dataList = sharedPreferencesManager.getHistoryList("history")


    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(150.dp)
        ) {
            HistoryChart(dataList)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.height(300.dp)
        ) {
            FilteredHistoryList(dataList)
            // Extract the rates map from the ApiRates instance and create a list with a single map
            var ratesList: List<Map<String, Double>>? = null

            if (currenciesRates != null) {
                ratesList = listOf(currenciesRates.rates)
            }

            if (ratesList != null) {
                ConversionList(ratesList)
            }
        }
    }


}


@Composable
fun HistoryItem(history: History) {


    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp)
            .background(Color.White)

    ) {
        Text(text = history.date, fontSize = 16.sp, textAlign = TextAlign.Start)
        Text(text = "Amount: ${history.amount}", fontSize = 14.sp, textAlign = TextAlign.Start)
        Text(text = "Result: ${history.result}", fontSize = 14.sp, textAlign = TextAlign.Start)
        Text(text = "From: ${history.from}", fontSize = 14.sp, textAlign = TextAlign.Start)
        Text(text = "To: ${history.to}", fontSize = 14.sp, textAlign = TextAlign.Start)
    }
}


@Composable
fun FilteredHistoryList(historyList: List<History>) {
    val threeDaysAgo = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -3)
    }

    LazyColumn {
        items(historyList.filter { history ->
            val historyDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(history.date)
            val historyCalendar = Calendar.getInstance().apply {
                time = historyDate
            }
            historyCalendar.after(threeDaysAgo) || historyCalendar == threeDaysAgo
        }) { history ->
            HistoryItem(history)
        }
    }
}


@Composable
fun HistoryChart(historyList: List<History>) {
    val maxValue = historyList.maxOfOrNull { it.amount.toFloat() } ?: 0f
    val yPoints = historyList.map { it.amount.toFloat() }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val xStep = canvasWidth / (historyList.size - 1).toFloat()

        val scaleX = (canvasWidth - 16.dp.toPx() * 2) / (historyList.size - 1).toFloat()
        val scaleY = (canvasHeight - 16.dp.toPx() * 2) / maxValue

        val path = Path()

        path.moveTo(16.dp.toPx(), canvasHeight - yPoints[0] * scaleY)

        for (i in 1 until yPoints.size) {
            val x = i * xStep + 16.dp.toPx()
            val y = canvasHeight - yPoints[i] * scaleY
            path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color.Blue.copy(alpha = 0.7f),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}



@Composable
fun ConversionList(conversionData: List<Map<String, Double>>) {
    var selectedBaseCurrency by remember { mutableStateOf(conversionData.first().keys.first()) }

    val listOfCurrencies = conversionData.take(10).first().keys.toList()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Dropdown to select the base currency
        DropdownMenuComponent(
            items = listOfCurrencies,
            selectedItem = selectedBaseCurrency,
            onItemSelected = {
                selectedBaseCurrency = it
            },
            modifier = Modifier.wrapContentWidth()
        )

        Text(
            text = "Conversions from $selectedBaseCurrency",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(conversionData) { dataMap ->

                val conversionRates = dataMap.filterKeys { it != selectedBaseCurrency }
                // Take the first 10 conversion rates
                val targetCurrencies = conversionRates.keys.toList().take(10)


                targetCurrencies.forEach { currency ->
                    val rate =  ConvertCurrency(1.0,selectedBaseCurrency,listOfCurrencies,conversionData)
                    rate?.let {
                        val convertedAmount = it
//                        * 10.0 // Convert 1 unit of base currency to 10 units of target currency
                        Text(
                            text = "1 $selectedBaseCurrency to $currency: ${convertedAmount[currency]}",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }

            }
        }
    }
}





fun ConvertCurrency(
    amount: Double,
    baseCurrency: String,
    targetCurrencies: List<String>,
    exchangeRateData: List<Map<String, Double>>
): Map<String, Double>? {
    val baseRates = exchangeRateData.firstOrNull { it.containsKey(baseCurrency) }

    if (baseRates != null) {
        val convertedAmounts = mutableMapOf<String, Double>()

        targetCurrencies.forEach { toCurrency ->
            val fromRate = baseRates[baseCurrency]
            val toRate = baseRates[toCurrency]

            if (fromRate != null && toRate != null) {
                val baseAmount = amount / fromRate
                val convertedAmount = roundDouble(baseAmount * toRate, 4)
                convertedAmounts[toCurrency] = convertedAmount
            }
        }

        return convertedAmounts
    }

    return null // Handle missing base rates
}




