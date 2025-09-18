package com.example.twitterfalso.data.datasource

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.twitterfalso.ui.functions.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

//Datasource: Firebase Auth
class AuthRemoteDataSource @Inject constructor (
    private val auth: FirebaseAuth
){

    // Permite realizar Sign in con usuario y contraseña
    suspend fun signIn(email: String, password: String): Unit {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    // Permite realizar Sign up con usuario y contraseña
    suspend fun signUp(email: String, password: String): Unit {
        Log.d("PRUEBA_REGISTER", "signUp: $email $password")
        auth.createUserWithEmailAndPassword(email, password).await()
    }


    //Permite cerrar sesion
    //No es necesario el suspend
    fun signOut(): Unit {
        auth.signOut()
    }

    //Permite actualizar la foto de perfil del usuario en FireAuth
    //Recibe la url de la imagen
    //Se debe llamar despues de subir la funcion a firebase
    suspend fun updateProfileImage(photoUrl: String): Unit {
        val uri = photoUrl.toUri()
        Utils.getCurrentUser()?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        )?.await()
    }

}