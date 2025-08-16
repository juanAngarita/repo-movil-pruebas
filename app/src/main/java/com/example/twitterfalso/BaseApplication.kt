package com.example.twitterfalso

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // 👈 inicializa Firebase

    }
}