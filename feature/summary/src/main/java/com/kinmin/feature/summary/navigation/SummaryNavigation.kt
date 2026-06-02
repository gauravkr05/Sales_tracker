package com.kinmin.feature.summary.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.summary.presentation.SummaryScreen

fun NavGraphBuilder.summaryScreen(onBack: () -> Unit) {
    composable(Routes.SUMMARY) { SummaryScreen(onBack = onBack) }
}
