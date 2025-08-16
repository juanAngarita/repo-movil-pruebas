package com.example.twitterfalso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
// Donde inicia la aplicacion
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TwitterFalsoTheme {
                TwitterApp()
            }
        }
    }
}

