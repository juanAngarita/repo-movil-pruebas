package com.example.twitterfalso.ui.Screens.Splash

import androidx.lifecycle.ViewModel
import com.example.twitterfalso.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _navigateHome = MutableStateFlow(false)
    val navigateHome: StateFlow<Boolean> = _navigateHome

    init {
        checkUser()
    }

    private fun checkUser(){
        if(authRepository.currentUser != null){
            _navigateHome.value = true
        } else {
            _navigateHome.value = false
        }

    }

}