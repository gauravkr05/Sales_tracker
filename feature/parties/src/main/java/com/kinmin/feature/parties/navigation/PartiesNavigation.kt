package com.kinmin.feature.parties.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.parties.presentation.PartiesScreen

fun NavGraphBuilder.partiesScreen(onBack: () -> Unit) {
    composable(Routes.PARTIES) { PartiesScreen(onBack = onBack) }
}
