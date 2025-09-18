package com.example.twitterfalso.dataSource

import android.util.Log
import com.example.twitterfalso.data.datasource.impl.firestore.TweetFirestoreDataSourceImpl
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.CreateTweetUserDto
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.UserProfileFirestoreDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FirebaseUserDatasourceTest {

    private val db = Firebase.firestore
    private lateinit var datasource: UserFirestoreDataSourceImpl
    private lateinit var tweetsDatasource: TweetFirestoreDataSourceImpl

    private fun generateUser(i: Int): UserProfileFirestoreDto = UserProfileFirestoreDto(
        id = "user_$i",
        username = "username_$i",
        email = "user$i@example.com",
        name = "Name $i",
        bio = "Bio de usuario $i",
        pais = "Colombia",
        website = "https://example.com/$i",
        profileImage = "https://picsum.photos/200/200?random=$i",
        coverImage = "https://picsum.photos/800/200?random=$i",
        birthDate = "1990-01-01",
        verified = (i % 2 == 0),
        followersCount = i * 10,
        followingCount = i * 5,
        createdAt = "2025-01-01T00:00:00Z",
        updatedAt = "2025-01-01T00:00:00Z",
        followed = false
    )

    @Before
    fun setup() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (
            e: Exception
        ) {

        }

        datasource = UserFirestoreDataSourceImpl(db)
        tweetsDatasource = TweetFirestoreDataSourceImpl(db)
        val users = db.collection("users").get().await()
        // limpiar antes de cada prueba
        for (userDoc in users) {
            // borrar subcolecciones
            val followers = userDoc.reference.collection("followers").get().await()
            for (f in followers) f.reference.delete().await()

            val following = userDoc.reference.collection("following").get().await()
            for (f in following) f.reference.delete().await()

            // borrar el documento principal
            userDoc.reference.delete().await()
        }

        //delete tweets
        val tweets = db.collection("tweets").get().await()
        for (tweetDoc in tweets) {
            tweetDoc.reference.delete().await()
        }

        // cargar 100 usuarios de prueba (incluyendo el "123")
        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }
        batch.commit().await()
    }

    @Test
    fun getUserById_validId_CorrectUser() = runTest {

        val id = "user_9"
        val name = "Name 9"

        val result = datasource.getUserById(id, "")
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo(name)
        Truth.assertThat(result?.id).isEqualTo(id)
    }

    @Test
    fun getUserById_invalidId_Null() = runTest {
        val result = datasource.getUserById("user_999", "")
        Truth.assertThat(result).isNull()
    }

    @Test
    fun followOrUnfollow_followUser_followedTrue() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        datasource.followOrUnfollow(currentUser.id, targetUser.id)

        val targetUserUpdated = datasource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserUpdated?.followed).isTrue()
    }

    @Test
    fun followOrUnfollow_followUser_followersCountIncrement() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        val oldData = datasource.getUserById(targetUser.id)

        datasource.followOrUnfollow(currentUser.id, targetUser.id)

        val targetUserUpdated = datasource.getUserById(targetUser.id)
        Truth.assertThat(targetUserUpdated?.followersCount).isGreaterThan(oldData?.followersCount)
    }



    @Test
    fun followOrUnfollow_unfollowUser_followedFalse() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        Log.d("TEST", "Primera llamada")
        datasource.followOrUnfollow(currentUser.id, targetUser.id)
        Log.d("TEST", "Segunda llamada")
        datasource.followOrUnfollow(currentUser.id, targetUser.id)
        Log.d("TEST", "Fin segunda llamada")
        val targetUserUpdated = datasource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserUpdated?.followed).isFalse()
    }

    @Test
    fun registerUser_insertsDocument_documentExists() = runTest {
        val registerUserDto = RegisterUserDto(
            username = "test_user",
            name = "Test User",
            pais = "Colombia",
            bio = "Bio de usuario",
            FCMToken = ""
        )
        val docRef = db.collection("users").document()
        docRef.set(registerUserDto).await()

        // Comprobamos que el ID no esté vacío
        Truth.assertThat(docRef.id).isNotEmpty()

        // Opcional: comprobar que el documento existe
        val userSearch = datasource.getUserById(
            id = docRef.id,
        )
        Truth.assertThat(userSearch).isNotNull()
        Truth.assertThat(userSearch?.name).isEqualTo(registerUserDto.name)
        Truth.assertThat(userSearch?.profileImage).isNull()
    }

    @Test
    fun getUserTweets_getTweetsList() = runTest {
        val userId = "user_1"
        val user = datasource.getUserById(userId)
        val createTweetDto = CreateTweetDto(
            content = "Test tweet",
            userId = userId,
            parentTweetId = null,
            tweeetId = null,
            user = CreateTweetUserDto(
                name = user?.name,
                username = user?.username,
                profileImage = user?.profileImage
            )
        )
        tweetsDatasource.createTweet(createTweetDto)
        val tweets = datasource.getUserTweets(userId)
        Truth.assertThat(tweets).isNotEmpty()
    }
}