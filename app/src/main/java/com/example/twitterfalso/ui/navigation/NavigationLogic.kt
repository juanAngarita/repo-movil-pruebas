package com.example.twitterfalso.ui.navigation

object NavigationLogic {

    //List of screen that should not show the top bar
    private val noTopBarScreens = listOf(
        Screen.Start.route,
        Screen.Register.route,
        Screen.Splash.route,
        Screen.Login.route,
        Screen.CreateTweet.route,
        Screen.UserProfile.route
    )

    //List of screens that should show the bottom bar
    private val showBottomBarScreens = listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Notifications.route,
        Screen.Settings.route,
        Screen.Profile.route
    )

    fun shouldShowTopBar(route: String?): Boolean {
        return route != null && noTopBarScreens.none { route.startsWith(it.substringBefore("/{")) }
    }

    fun shouldShowBottomBar(route: String?): Boolean {
        return route != null && showBottomBarScreens.any { route.startsWith(it.substringBefore("/{")) }
    }

}