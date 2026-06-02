package com.kinmin.feature.remarks.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.remarks.presentation.RemarksScreen

fun NavGraphBuilder.remarksScreen(onDone: () -> Unit) {
    composable(Routes.REMARKS) { RemarksScreen(onDone = onDone) }
}
