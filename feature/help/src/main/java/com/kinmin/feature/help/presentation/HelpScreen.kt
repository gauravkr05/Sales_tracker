package com.kinmin.feature.help.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HelpScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Help & Contact", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("For any issue with the app, contact your supervisor.")
        Text("Office: +91-XXXXXXXXXX", fontWeight = FontWeight.Medium)
        Text("Support hours: Mon–Sat, 9 AM – 6 PM")
    }
}
