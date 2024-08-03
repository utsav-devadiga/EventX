package com.applabs.eventx.events.util

/**
 * @author Utsav Devadiga
 */
sealed class Screen(val route: String) {
    object Home : Screen("main")
    object DashboardEvents : Screen("dashboard")
    object EventList : Screen("eventList")
    object Details : Screen("details")
}
