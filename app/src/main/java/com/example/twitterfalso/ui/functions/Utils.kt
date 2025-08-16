package com.example.twitterfalso.ui.functions

import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

object Utils {

    fun getCurrentUserPhoto(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.photoUrl?.toString() ?: ""
        return photoUrl
    }

    fun getCurrentUserId(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.uid ?: ""
        return photoUrl
    }

    fun getCurrentRoute(navController: NavHostController): String {
        return navController.currentBackStackEntry?.destination?.route ?: ""
    }

}