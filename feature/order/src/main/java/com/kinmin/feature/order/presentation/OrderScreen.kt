package com.kinmin.feature.order.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinmin.core.camera.CameraPreview
import com.kinmin.core.camera.CaptureController
import com.kinmin.core.camera.Watermark
import com.kinmin.core.designsystem.KinminPrimaryButton

@Composable
fun OrderScreen(
    onDone: () -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var party by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var controller by remember { mutableStateOf<CaptureController?>(null) }

    LaunchedEffect(state.saved) { if (state.saved) onDone() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Take order", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Box(Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {
            CameraPreview(modifier = Modifier.fillMaxSize()) { controller = it }
        }

        KinminPrimaryButton(
            text = if (state.photo == null) "CAPTURE PHOTO" else "RETAKE PHOTO"
        ) {
            controller?.takePicture(
                onResult = { bmp ->
                    // GPS values would come from the last known location; null-safe here.
                    val file = Watermark.stamp(bmp, context.filesDir, lat = null, lng = null)
                    viewModel.setPhoto(file)
                },
                onError = { /* surface in UI in production */ }
            )
        }

        if (state.photo != null) {
            Text("Photo captured \u2713", color = Color(0xFF1F6E5E))
        }

        OutlinedTextField(value = party, onValueChange = { party = it },
            label = { Text("Party / retailer name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = amount, onValueChange = { amount = it },
            label = { Text("Order amount (\u20B9)") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(Modifier.height(4.dp))
        KinminPrimaryButton(
            text = if (state.saving) "Saving…" else "SAVE ORDER",
            enabled = !state.saving
        ) { viewModel.submit(party, amount) }

        state.error?.let { Text(it, color = Color.Red, fontSize = 13.sp) }
    }
}