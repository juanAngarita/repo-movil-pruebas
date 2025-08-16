package com.example.twitterfalso.ui.Screens.Splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToStart: () -> Unit,
    splashViewModel: SplashViewModel,
    modifier: Modifier = Modifier.fillMaxSize()
) {

    Box(
        modifier = modifier
    )

    val navigateHome by splashViewModel.navigateHome.collectAsState()

    if(navigateHome) {
        navigateToHome()
    } else {
        navigateToStart()
    }

}