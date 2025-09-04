package com.example.twitterfalso

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
import org.junit.After
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {

    private val db = Firebase.firestore

    private lateinit var dataSource: UserFirestoreDataSourceImpl

    private lateinit var tweetDatasource: TweetFirestoreDataSourceImpl

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
        followed = false,
    )

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {

        }


        dataSource = UserFirestoreDataSourceImpl(db)
        tweetDatasource = TweetFirestoreDataSourceImpl(db)

        //replicables
        //autonomas

        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }
        Log.d("TAG", "Antes del batch")
        batch.commit().await()
        Log.d("TAG", "Después del batch")
    }


    @Test
    fun getUserById_validId_correctUser() = runTest {

        //AAA
        //Arrange
        val id = "user_9"
        val expectedName = "Name 9"
        //Act
        val result = dataSource.getUserById(id, "")
        //Assert
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo(expectedName)
        Truth.assertThat(result?.id).isEqualTo(id)
    }

    @Test
    fun getUserById_invalidId_null() = runTest {
        //AAA
        //Arrange
        val id = "user_999"
        //Act
        val result = dataSource.getUserById(id, "")
        //Assert
        Truth.assertThat(result).isNull()
    }

    @Test
    fun registerUser_insertDocument_DocumentExists() = runTest {
        //AAA
        //Arrange
        val user = RegisterUserDto(
            username = "username",
            name = "name",
            pais = "pais",
            bio = "bio",
            FCMToken = ""
        )
        //Act
        dataSource.registerUser(user, "999")

        //assert
        val result = dataSource.getUserById("999", "")
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo("name")
        Truth.assertThat(result?.id).isEqualTo("999")
    }

    @Test
    fun followOrUnfollowUser_followUser_UserFollowed() = runTest {
        //AAA
        //Arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        //Act
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        //assert
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followed).isTrue()
    }

    @Test
    fun followOrUnfollowUser_followUser_followersCountIncrement() = runTest {
        //AAA
        //Arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        val oldData = dataSource.getUserById(targetUser.id)

        //Act
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        //assert
        val targetUserResult = dataSource.getUserById(targetUser.id)
        Truth.assertThat(targetUserResult?.followersCount).isGreaterThan(oldData?.followersCount)
    }


    @Test
    fun followOrUnfollowUser_unfollow_followedFalse() = runTest {
        //AAA
        //Arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        //Act
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        //assert
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followed).isFalse()
    }


    @After
    fun tearDown() = runTest {
        val users = db.collection("users").get().await()

        for(userDoc in users){
            val followers = userDoc.reference.collection("followers").get().await()
            for(follower in followers){
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for(f in following){
                f.reference.delete().await()
            }
        }


        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }

    @Test
    fun getUserTweets_validId_correctTweets() = runTest {
        //AAA
        val user = generateUser(1)

        val createTweetDto = CreateTweetDto(
            content = "Hola Mundo",
            userId = user.id,
            parentTweetId = null,
            tweeetId = null,
            user = CreateTweetUserDto(
                name = user.name,
                username = user.username,
                profileImage = user.profileImage
            )
        )

        tweetDatasource.createTweet(createTweetDto)

        val tweets = dataSource.getUserTweets(user.id)
        Truth.assertThat(tweets).isNotEmpty()
        Truth.assertThat(tweets.first().content).isEqualTo(createTweetDto.content)
    }

}