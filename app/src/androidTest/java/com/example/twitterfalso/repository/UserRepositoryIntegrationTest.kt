package com.example.twitterfalso.repository

import android.util.Log
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.TweetFirestoreDataSourceImpl
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.dtos.UserProfileFirestoreDto
import com.example.twitterfalso.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private lateinit var userRepository: UserRepository

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

        userRepository = UserRepository(UserFirestoreDataSourceImpl(db), AuthRemoteDataSource(auth))

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
        //arrange
        val id = "user_9"
        val expectedName = "Name 9"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result?.getOrNull()?.name).isEqualTo(expectedName)
    }

    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result?.exceptionOrNull()?.message).isEqualTo("User not found")
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

}