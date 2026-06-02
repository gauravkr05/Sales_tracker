package com.kinmin.tracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kinmin.core.navigation.Routes
import com.kinmin.feature.activity.navigation.activityScreen
import com.kinmin.feature.auth.navigation.authScreen
import com.kinmin.feature.help.navigation.helpScreen
import com.kinmin.feature.home.navigation.homeScreen
import com.kinmin.feature.order.navigation.orderScreen
import com.kinmin.feature.parties.navigation.partiesScreen
import com.kinmin.feature.remarks.navigation.remarksScreen
import com.kinmin.feature.summary.navigation.summaryScreen

/**
 * The ONLY place features are wired together. Each feature exposes one
 * NavGraphBuilder extension. Adding a feature = one import + one line here.
 * Removing a feature = delete its module + delete its line. Nothing else breaks.
 */
@Composable
fun KinminNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.AUTH) {

        authScreen(
            onLoggedIn = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.AUTH) { inclusive = true }
                }
            }
        )

        homeScreen(
            onNavigate = { route -> navController.navigate(route) },
            onLogout = {
                navController.navigate(Routes.AUTH) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )

        orderScreen(onDone = { navController.popBackStack() })
        activityScreen(onDone = { navController.popBackStack() })
        remarksScreen(onDone = { navController.popBackStack() })
        summaryScreen(onBack = { navController.popBackStack() })
        partiesScreen(onBack = { navController.popBackStack() })
        helpScreen(onBack = { navController.popBackStack() })
    }
}
