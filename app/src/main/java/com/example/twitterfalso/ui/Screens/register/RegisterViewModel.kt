package com.example.twitterfalso.ui.Screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.injection.IoDispatcher
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.functions.Utils
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

//- controlar los datos que se muestran en la pantalla
//- logica de negocio/eventos
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel() {


    val _uistate = MutableStateFlow(RegisterFormState())
    val uistate: StateFlow<RegisterFormState> = _uistate

    fun updateEmail(input: String) {
        _uistate.update {
            it.copy(email = input)
        }
    }

    fun updateName(input: String) {
        _uistate.update {
            it.copy(name = input)
        }
    }

    fun updatePassword(input: String) {
        _uistate.update {
            it.copy(password = input)
        }
    }

    fun updateUserName(input: String) {
        _uistate.update {
            it.copy(userName = input)
        }
    }

    fun updateBio(input: String) {
        _uistate.update {
            it.copy(bio = input)
        }
    }

    fun updatePais(input: String) {
        _uistate.update {
            it.copy(pais = input)
        }
    }

    fun updateFechaNacimiento(input: LocalDate) {
        _uistate.update {
            it.copy(fechaNacimiento = input)
        }
    }


    fun mostrarEsconderPassword() {
        val valorActual = _uistate.value.mostrarPassword
        _uistate.update { it.copy(mostrarPassword = !valorActual) }
    }

    fun registerButtonPressed() {
        val state = _uistate.value
        // Validaciones previas
        when {
            state.email.isBlank() || state.password.isBlank() -> {
                showError("Todos los campos son obligatorios")
                return
            }
            state.password.length < 6 -> {
                showError("La contraseña debe tener al menos 6 caracteres")
                return
            }
        }

        // Registro en Firebase
        viewModelScope.launch(
            ioDispatcher
        ) {
            registerUserOnline()
        }
    }

    suspend fun registerUserOnline() {
        _uistate.update { it.copy(loading = true) }

        val state = _uistate.value

        val result = authRepository.signUp(state.email, state.password)

        result.onSuccess {
            val userId = Utils.getCurrentUserId()
            userRepository.registerUser(
                username = state.userName,
                pais = state.pais,
                name = state.name,
                bio = state.bio,
                userId = userId
            ).onSuccess {
                _uistate.update { it.copy(navigate = true, loading = false) }
            }.onFailure {
                _uistate.update { it.copy(navigate = false, loading = false) }
                showError(it.message ?: "Error en Firestore")
            }
        }.onFailure{ e ->
            when (e) {
                is FirebaseAuthUserCollisionException -> showError("El correo electrónico ya está en uso")
                is FirebaseAuthInvalidCredentialsException -> showError("El correo electrónico no es válido")
                else -> showError(e.message ?: "Error al registrar")
            }
        }
    }

    private fun showError(message: String) {
        _uistate.update {
            it.copy(
                errorMessage = message,
                mostrarMensaje = true,
                loading = false
            )
        }
    }
}

