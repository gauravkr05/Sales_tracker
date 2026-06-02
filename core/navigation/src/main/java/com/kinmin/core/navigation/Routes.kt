package com.kinmin.core.navigation

/**
 * Central route registry. Features reference routes through these constants
 * instead of importing each other -> zero compile-time coupling between features.
 */
object Routes {
    const val AUTH = "auth"
    const val HOME = "home"
    const val ORDER = "order"
    const val ACTIVITY = "activity"
    const val REMARKS = "remarks"
    const val SUMMARY = "summary"
    const val PARTIES = "parties"
    const val HELP = "help"
}
