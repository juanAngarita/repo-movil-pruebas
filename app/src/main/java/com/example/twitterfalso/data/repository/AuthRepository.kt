package com.example.twitterfalso.data.repository

import android.util.Log
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.ui.Screens.register.RegisterFormState
import com.example.twitterfalso.ui.functions.Utils
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//Repositorio: Autenticacion
//Inyeccion de dependencias: AuthRemoteDataSource

/*
* Cuando Las funciones son de salida
* Entrada: datos desde el viewmodel
* Proceso: Genera el Dto
* Salida: Result
* */

/*
* Cuando las funciones son de entrada
* Entrada: DTO desde el datasource
* Proceso: Retorna un Result con un objeto de UI
* */
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRepository: UserRepository
) {

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.signIn(email, password)
             Result.success(Unit)
        }
        catch (e: FirebaseAuthInvalidCredentialsException){
             Result.failure(Exception("Credenciales incorrectas"))
        } catch (e: FirebaseAuthInvalidUserException) {
             Result.failure(Exception("El usuario no existe"))
        }
        catch (e: Exception) {
             Result.failure(Exception("Error al iniciar sesion"))
        }

    }


    suspend fun signUp(email: String, password: String): Result<Unit> {
        try {
            authRemoteDataSource.signUp(email, password)
            return Result.success(Unit)
        }
        catch (e: Exception) {
            return Result.failure(e)
        }
    }



    fun signOut() {
        authRemoteDataSource.signOut()
    }



}