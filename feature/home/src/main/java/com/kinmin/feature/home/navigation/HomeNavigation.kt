package com.kinmin.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.home.presentation.HomeScreen

fun NavGraphBuilder.homeScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    composable(Routes.HOME) {
        HomeScreen(onNavigate = onNavigate, onLogout = onLogout)
    }
}
