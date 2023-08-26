package com.ahmedalamin.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedalamin.domain.entity.History

@Composable
fun DetailsScreen(sharedPreferencesManager: SharedPreferencesManager) {


    val dataList = sharedPreferencesManager.getHistoryList("history")

    LazyColumn {
        items(dataList) { history ->
            HistoryItem(history)
        }
    }

}


@Composable
fun HistoryItem(history: History) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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