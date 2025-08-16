package com.example.twitterfalso.ui.Screens.login

//
data class LoginState(
    val email: String = "",
    val password: String = "",
    val mostrarContrasena: Boolean = false,
    val errorMessage: String = "",
    val mostrarMensaje: Boolean = false,
    val navigate: Boolean = false
)
