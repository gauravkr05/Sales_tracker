package com.kinmin.feature.remarks.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinmin.core.designsystem.KinminPrimaryButton

@Composable
fun RemarksScreen(onDone: () -> Unit, viewModel: RemarksViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var text by remember { mutableStateOf("") }
    LaunchedEffect(state.saved) { if (state.saved) onDone() }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Day remarks", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(text, { text = it }, label = { Text("Notes about your day") },
            modifier = Modifier.fillMaxWidth().height(180.dp))
        KinminPrimaryButton(if (state.saving) "Saving…" else "SAVE", enabled = !state.saving) { viewModel.submit(text) }
        state.error?.let { Text(it, color = Color.Red, fontSize = 13.sp) }
    }
}
