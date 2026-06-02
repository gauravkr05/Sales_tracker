package com.kinmin.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.kinmin.core.designsystem.KinminTheme
import com.kinmin.tracker.navigation.KinminNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KinminTheme {
                val navController = rememberNavController()
                KinminNavHost(navController = navController)
            }
        }
    }
}
