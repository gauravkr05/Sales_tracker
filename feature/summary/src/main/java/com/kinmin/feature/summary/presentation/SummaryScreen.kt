package com.kinmin.feature.summary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SummaryScreen(onBack: () -> Unit, viewModel: SummaryViewModel = hiltViewModel()) {
    val s by viewModel.state.collectAsState()
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("My day", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        StatCard("Orders today", s.orderCount.toString())
        StatCard("Order value", "\u20B9${"%,.0f".format(s.totalValue)}")
        StatCard("Waiting to sync", s.pending.toString())
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(label, fontSize = 13.sp)
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }
    }
}
