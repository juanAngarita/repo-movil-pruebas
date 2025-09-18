package com.example.twitterfalso.data.datasource.impl.firestore

import android.util.Log
import com.example.twitterfalso.data.datasource.UserRemoteDataSource
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.TweetDto
import com.example.twitterfalso.data.dtos.UpdateUserDto
import com.example.twitterfalso.data.dtos.UserDtoGeneric
import com.example.twitterfalso.data.dtos.UserProfileFirestoreDto
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {

    override suspend fun getUserById(id: String, currentUserId: String): UserProfileFirestoreDto? {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        val user =  respuesta.toObject(UserProfileFirestoreDto::class.java) ?: return null

        user.copy(id = id)
        if(currentUserId.isNotEmpty()){
            val followerDoc = db.collection("users").document(id).collection("followers").document(currentUserId).get().await()
            val exist = followerDoc.exists()
            user.followed = exist
        }
        return user
    }

    override suspend fun getUserTweets(id: String): List<TweetDto> {
        val snapshot = db.collection("tweets").whereEqualTo("userId", id).get().await()
        return snapshot.documents.map { doc ->
            val tweet = doc.toObject(TweetDto::class.java)
            tweet?.copy(id = doc.id) ?: throw Exception("Tweet not found")
        }
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        Log.d("TAGPRUEBAS", "Entrando a registerUser de user datasource")
        val docRef = db.collection("users").document(userId) //se crea un documento
        Log.d("TAGPRUEBAS", "Entrando a registerUser de user datasource2")

        docRef.set(registerUserDto).await() // insertando el documento
    }


    override suspend fun updateUserProfileImage(userId: String, photoUrl: String) {
        val docRef = db.collection("users").document(userId)
        docRef.update("profileImage", photoUrl).await()
    }



    override suspend fun followOrUnfollow(currentUserId: String, targetUserId: String) {
        Log.d("TEST", "currentUserId: $currentUserId")
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followerRef = targetUserRef.collection("followers").document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {
                Log.d("TEST", "Si existe")
                // ❌ Dejar de seguir
                transaction.delete(followingRef)
                transaction.delete(followerRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                Log.d("TEST", "No existe")
                // ✅ Seguir
                transaction.set(followingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followerRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }.await()
        Log.d("TEST", "Fin de follow")
    }

    override suspend fun updateUserProfile(
        userId: String,
        userProfileInfo: UpdateUserDto
    ) {
        val docRef = db.collection("users").document(userId)
        docRef.update(
            mapOf(
                "name" to userProfileInfo.name,
                "bio" to userProfileInfo.bio,
                "location" to userProfileInfo.pais
            )
        ).await()
    }

    override suspend fun updateUserBackgroundImage(userId: String, photoUrl: String) {
        val docRef = db.collection("users").document(userId)
        docRef.update("coverImage", photoUrl).await()
    }

    override suspend fun searchUsers(query: String): List<UserProfileFirestoreDto> {
        if (query.isBlank()) return emptyList()

        val snapshot = db.collection("users")
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .limit(10)
                .get()
                .await()
        val users = snapshot.toObjects(UserProfileFirestoreDto::class.java)

        return users
    }
}