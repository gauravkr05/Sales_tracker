package com.kinmin.feature.activity.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.activity.presentation.ActivityScreen

fun NavGraphBuilder.activityScreen(onDone: () -> Unit) {
    composable(Routes.ACTIVITY) { ActivityScreen(onDone = onDone) }
}
