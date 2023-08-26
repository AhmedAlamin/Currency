package com.ahmedalamin.currency


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedalamin.domain.entity.History
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DetailsScreen(sharedPreferencesManager: SharedPreferencesManager) {


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
            FilteredHistoryList(dataList)
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




