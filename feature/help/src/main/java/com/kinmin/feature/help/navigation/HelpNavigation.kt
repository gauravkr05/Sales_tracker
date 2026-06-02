package com.kinmin.feature.help.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.help.presentation.HelpScreen

fun NavGraphBuilder.helpScreen(onBack: () -> Unit) {
    composable(Routes.HELP) { HelpScreen(onBack = onBack) }
}
