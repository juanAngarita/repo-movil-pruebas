package com.example.twitterfalso.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//Datasource: Firebase Storage
class StorageRemoteDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {

    //Permite subir una imagen a Firebase Storage
    //Path: ubicacion/carpeta
    //Uri: ruta de la imagen en local
    suspend fun uploadImage(path: String, uri: Uri): String {
        val imageRef = storage.reference.child(path)
        imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }

}