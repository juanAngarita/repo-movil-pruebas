package com.example.twitterfalso.data.repository

import android.net.Uri
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.StorageRemoteDataSource
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource
) {

    suspend fun uploadProfileImage(uri:Uri): Result<String> {
        return try{
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("No user logged in"))

            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)

            //Actualizar uerl del usuario
            auth.updateProfileImage(url)

            Result.success(url)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}