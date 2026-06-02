package com.kinmin.feature.parties.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Demo data; replace with a PartiesRepository backed by Supabase "parties" table.
private val demoParties = listOf(
    "Sharma Stores — Boring Road",
    "Gupta Provision — Kankarbagh",
    "Maa Vaishno Kirana — Patliputra",
    "New Bharat Traders — Rajendra Nagar"
)

@Composable
fun PartiesScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Parties / Retailers", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp))
        LazyColumn {
            items(demoParties) { p ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(p, Modifier.padding(16.dp))
                }
            }
        }
    }
}
