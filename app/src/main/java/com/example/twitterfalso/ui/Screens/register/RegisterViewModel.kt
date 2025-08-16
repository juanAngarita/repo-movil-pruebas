package com.example.twitterfalso.ui.Screens.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

//- controlar los datos que se muestran en la pantalla
//- logica de negocio/eventos
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
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
        viewModelScope.launch {
            val result = authRepository.signUp(state.email, state.password)

            if (result.isSuccess) {

                val userId = authRepository.currentUser?.uid ?: throw Exception("No se pudo obtener el usuario actual")

                userRepository.registerUser(
                    username = state.userName,
                    pais = state.pais,
                    name = state.name,
                    bio = state.bio,
                    userId = userId
                )


                _uistate.update { it.copy(navigate = true) }
            } else {
                val mensaje = result.exceptionOrNull()?.message ?: "Error al registrar"
                showError(mensaje)
            }
        }
    }

    private fun showError(message: String) {
        _uistate.update {
            it.copy(
                errorMessage = message,
                mostrarMensaje = true
            )
        }
    }
}

