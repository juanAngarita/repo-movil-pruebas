package com.example.twitterfalso.data.local

import com.google.firebase.firestore.FirebaseFirestore

object SeedHelper {

    fun seedUsersAndTweets(db: FirebaseFirestore) {
        val users = (1..10).map { i ->
            val user = mapOf(
                "id" to i,
                "username" to "user_$i",
                "email" to "user_$i@example.com",
                "name" to "User $i",
                "bio" to "Bio de prueba $i",
                "pais" to "Colombia",
                "followersCount" to (0..100).random(),
                "followingCount" to (0..50).random(),
                "createdAt" to System.currentTimeMillis()
            )
            db.collection("users").document(i.toString()).set(user)
        }

        // Tweets por usuario
        (1..10).forEach { userId ->
            (1..5).forEach { j ->
                val tweet = mapOf(
                    "id" to "${userId}_$j",
                    "userId" to "$userId",
                    "content" to "Tweet $j del usuario $userId"
                )
                db.collection("tweets").document("${userId}_$j").set(tweet)
            }
        }
    }
}