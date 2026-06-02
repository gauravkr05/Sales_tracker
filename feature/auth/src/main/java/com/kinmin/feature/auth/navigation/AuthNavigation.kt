package com.kinmin.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.auth.presentation.AuthScreen

fun NavGraphBuilder.authScreen(onLoggedIn: () -> Unit) {
    composable(Routes.AUTH) { AuthScreen(onLoggedIn = onLoggedIn) }
}
