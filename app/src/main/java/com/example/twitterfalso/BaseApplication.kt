package com.example.twitterfalso

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp
import com.example.twitterfalso.BuildConfig
import com.example.twitterfalso.data.local.SeedHelper

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // 👈 inicializa Firebase

        if (BuildConfig.DEBUG) {
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Log.d("FIREBASE", "Emulando Firestore")
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            SeedHelper.seedUsersAndTweets(Firebase.firestore)
        }

    }
}