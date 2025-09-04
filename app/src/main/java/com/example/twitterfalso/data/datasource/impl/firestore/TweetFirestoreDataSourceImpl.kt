package com.example.twitterfalso.data.datasource.impl.firestore

import com.example.twitterfalso.data.datasource.TweetRemoteDataSource
import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.TweetDto
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

        val tweetSnapshot = tweetRef.get().await()
        val tweet = tweetSnapshot.toObject(TweetDto::class.java) ?: throw Exception("Tweet not found")

        if(currentUserId.isNotEmpty()){
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

    override suspend fun sendOrDeleteTweetLike(tweetId: String, userId: String) {
        val tweetRef = db.collection("tweets").document(tweetId)
        val likesRef = tweetRef.collection("likes").document(userId)

        db.runTransaction{ transaction ->

            val likeDoc = transaction.get(likesRef)

            if(likeDoc.exists()){
                transaction.delete(likesRef)
                transaction.update(tweetRef, "likesCount", FieldValue.increment(-1))
            } else{
                transaction.set(likesRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(tweetRef, "likesCount", FieldValue.increment(1))
            }



        }



    }

    override suspend fun listenAllTweets(): Flow<List<TweetDto>> = callbackFlow {

        val listener = db.collection("tweets").addSnapshotListener{ snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null){
                val tweets = snapshot.documents.map { doc ->
                    val tweet = doc.toObject(TweetDto::class.java)
                    tweet?.copy(id = doc.id) ?: throw Exception("Tweet not found")
                }

                trySend(tweets).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }

}