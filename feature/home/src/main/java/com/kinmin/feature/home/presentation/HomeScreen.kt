package com.kinmin.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinmin.core.designsystem.KinminCream
import com.kinmin.core.designsystem.KinminOrange
import com.kinmin.core.designsystem.KinminPrimaryButton
import com.kinmin.core.designsystem.KinminTile
import com.kinmin.core.navigation.Routes

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = KinminCream),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Hello ${state.name}",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Medium
            )
        }

        KinminPrimaryButton(text = if (state.checkedIn) "CHECK OUT" else "CHECK IN") {
            viewModel.toggleCheckIn(state.checkedIn)
        }

        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            KinminTile("Order", "Take order", Icons.Filled.Receipt) { onNavigate(Routes.ORDER) }
            KinminTile("Activity", "Log meeting", Icons.Filled.TaskAlt) { onNavigate(Routes.ACTIVITY) }
            KinminTile("Remarks", "Day notes", Icons.Filled.Description) { onNavigate(Routes.REMARKS) }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            KinminTile("My day", "Summary", Icons.Filled.Insights) { onNavigate(Routes.SUMMARY) }
            KinminTile("Parties", "Retailers", Icons.Filled.Groups) { onNavigate(Routes.PARTIES) }
            KinminTile("Help", "Contact", Icons.Filled.HelpOutline) { onNavigate(Routes.HELP) }
        }

        Spacer(Modifier.height(8.dp))
        androidx.compose.material3.TextButton(onClick = onLogout) {
            Text("Log out", color = KinminOrange, fontSize = 14.sp)
        }
    }
}
