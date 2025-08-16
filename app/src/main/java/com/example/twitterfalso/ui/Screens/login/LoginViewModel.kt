package com.example.twitterfalso.ui.Screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//- controlar los datos que se muestran en la pantalla
//- logica de negocio/eventos
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun updateEmail(input: String){
        _uiState.update { it.copy(email = input) }
    }

    fun updatePassword(input: String){
        _uiState.update { it.copy(password = input) }
    }


    fun mostrarEsconderPassword(){
        val valorActual = _uiState.value.mostrarContrasena
        _uiState.update { it.copy(mostrarContrasena = !valorActual) }
    }


    fun registerButtonPressed(){
        if(
            _uiState.value.email.isNullOrEmpty() ||
            _uiState.value.password.isNullOrEmpty()
        ){
            _uiState.update { it.copy(mostrarMensaje = true, errorMessage = "Todos los campos son obligatorios") }
        } else {
            viewModelScope.launch {

                    val result = authRepository.signIn(
                        _uiState.value.email,
                        _uiState.value.password
                    )

                if(result.isSuccess) {
                    _uiState.update { it.copy(navigate = true) }
                } else {
                    val mensaje = result.exceptionOrNull()?.message ?: "Error al iniciar sesion"
                    _uiState.update { it.copy(errorMessage = mensaje, mostrarMensaje = true) }
                }

            }

        }
    }

}