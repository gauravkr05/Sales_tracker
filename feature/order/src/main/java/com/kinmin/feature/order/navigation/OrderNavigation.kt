package com.kinmin.feature.order.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.order.presentation.OrderScreen

fun NavGraphBuilder.orderScreen(onDone: () -> Unit) {
    composable(Routes.ORDER) { OrderScreen(onDone = onDone) }
}
