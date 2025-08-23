package com.example.twitterfalso.data.repository

import android.net.Uri
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.StorageRemoteDataSource
import com.example.twitterfalso.ui.functions.Utils
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource
) {

    suspend fun uploadProfileImage(uri:Uri): Result<String> {
        return try{
            val userId = Utils.getCurrentUserId()
            val path = "profileImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)
            //Actualizar uerl del usuario
            auth.updateProfileImage(url)
            Result.success(url)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun uploadBackgroundImage(uri:Uri): Result<String> {
        return try{
            val userId = Utils.getCurrentUserId()
            val path = "backgroundImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)
            Result.success(url)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}