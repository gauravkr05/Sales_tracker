package com.kinmin.feature.activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinmin.core.camera.CameraPreview
import com.kinmin.core.camera.CaptureController
import com.kinmin.core.camera.Watermark
import com.kinmin.core.designsystem.KinminPrimaryButton

@Composable
fun ActivityScreen(
    onDone: () -> Unit,
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var controller by remember { mutableStateOf<CaptureController?>(null) }

    LaunchedEffect(state.saved) { if (state.saved) onDone() }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Log activity", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Box(Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {
            CameraPreview(Modifier.fillMaxSize()) { controller = it }
        }
        KinminPrimaryButton(if (state.photo == null) "CAPTURE PHOTO" else "RETAKE") {
            controller?.takePicture(
                onResult = { bmp ->
                    viewModel.setPhoto(Watermark.stamp(bmp, context.filesDir, null, null))
                },
                onError = {}
            )
        }
        if (state.photo != null) Text("Photo captured \u2713", color = Color(0xFF1F6E5E))
        OutlinedTextField(title, { title = it }, label = { Text("What did you do?") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(note, { note = it }, label = { Text("Notes (optional)") }, modifier = Modifier.fillMaxWidth())
        KinminPrimaryButton(if (state.saving) "Saving…" else "SAVE ACTIVITY", enabled = !state.saving) {
            viewModel.submit(title, note)
        }
        state.error?.let { Text(it, color = Color.Red, fontSize = 13.sp) }
    }
}