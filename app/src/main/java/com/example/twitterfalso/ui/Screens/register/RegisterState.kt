package com.example.twitterfalso.ui.Screens.register

import java.time.LocalDate

data class RegisterFormState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val userName: String = "",
    val pais: String? = null,
    val bio: String? = null,
    val fechaNacimiento: LocalDate? = null,
    //Campos verificacion
    val mostrarPassword: Boolean = false,
    val navigate: Boolean = false,
    val errorMessage: String = "",
    val mostrarMensaje: Boolean = false
)

