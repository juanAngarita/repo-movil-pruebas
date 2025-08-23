package com.example.twitterfalso.data.datasource.impl.firestore

import com.example.twitterfalso.data.datasource.TweetRemoteDataSource
import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.TweetDto
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TweetFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : TweetRemoteDataSource {
    override suspend fun getAllTweets(): List<TweetDto> {
       val snapshot =  db.collection("tweets").get().await()
        return snapshot.documents.map { doc ->
            val tweet = doc.toObject(TweetDto::class.java)
            tweet?.copy(id = doc.id) ?: throw Exception("Tweet not found")
        }
    }



    override suspend fun getTweetById(id: String, currentUserId: String): TweetDto {
        val tweetRef = db.collection("tweets").document(id)

        // Obtiene el tweet
        val tweetSnapshot = tweetRef.get().await()
        val tweet = tweetSnapshot.toObject(TweetDto::class.java) ?: throw Exception("Tweet not found")

        if(currentUserId.isNotEmpty()){
            // Verifica si el usuario ya dio like
            val likeSnapshot = tweetRef.collection("likes").document(currentUserId).get().await()
            val hasLiked = likeSnapshot.exists()

            if(hasLiked){
                tweet.liked = true
            }
        }

        return tweet
    }

    override suspend fun createTweet(tweet: CreateTweetDto) {
        db.collection("tweets").add(tweet).await()
    }

    override suspend fun deleteTweet(id: String): Unit {
        db.collection("tweets").document(id).delete().await()

        val repliesSnapshot = db.collection("tweets").whereEqualTo("parentTweetId", id).get().await()

        for(doc in repliesSnapshot.documents){
            deleteTweet(doc.id)
        }
    }

    override suspend fun updateTweet(id: String, tweet: CreateTweetDto) {
        db.collection("tweets").document(id).set(tweet).await()


    }

    override suspend fun getTweetReplies(id: String): List<TweetDto> {
        val snapshot = db.collection("tweets").whereEqualTo("parentTweetId", id).get().await()

        return snapshot.documents.map { doc ->
            val tweet = doc.toObject(TweetDto::class.java)
            tweet?.copy(id = doc.id) ?: throw Exception("Tweet not found")
        }
    }

    override suspend fun sendOrDeleteLike(tweetId: String, userId: String) {
        val tweetRef = db.collection("tweets").document(tweetId)
        val likeRef = tweetRef.collection("likes").document(userId)
        //Lee y escribe de forma atómica → o se ejecuta todo o no se ejecuta nada.
        //Evita inconsistencias si dos usuarios modifican el mismo tweet al mismo tiempo.
        db.runTransaction { transaction ->
            val likeDoc = transaction.get(likeRef)
            if (likeDoc.exists()) {
                // Quitar like
                transaction.delete(likeRef) // Elimina el documento de la subcolección
                transaction.update(tweetRef, "likesCount", FieldValue.increment(-1)) // Resta 1 al contador
            } else {
                // Dar like
                transaction.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp())) // Crea el doc del like
                transaction.update(tweetRef, "likesCount", FieldValue.increment(1)) // Suma 1 al contador
            }
        }

    }

    override fun listenAllTweets(): Flow<List<TweetDto>> = callbackFlow {
        val listenerRegistration = db.collection("tweets")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // cierra el flujo con error
                    return@addSnapshotListener
                }
                if (snapshot != null) {


                    val tweets = snapshot.documents.map { doc ->
                        val tweet = doc.toObject(TweetDto::class.java)
                        tweet?.copy(id = doc.id) ?: throw Exception("Tweet not found")
                    }
                    trySend(tweets).isSuccess // envía la lista al Flow
                }
            }

        // Cuando el Flow se cancela, removemos el listener
        awaitClose { listenerRegistration.remove() }
    }

}