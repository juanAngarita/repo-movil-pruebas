package com.example.twitterfalso.repository

import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.dtos.UserProfileFirestoreDto
import com.example.twitterfalso.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    val db = Firebase.firestore

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

    private lateinit var userRepository: UserRepository

    @Before
    fun setup() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (
            e: Exception
        ) {
        }

        val users = db.collection("users").get().await()

        // Inicializar el repositorio con el DataSource usando la DB ya conectada
        userRepository = UserRepository(UserFirestoreDataSourceImpl(db))

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
    fun getUserById_validId_returnsCorrectUser() = runTest {
        // Act
        val result = userRepository.getUserById("user_1")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.name).isEqualTo("Name 1")
    }

    @Test
    fun getUserById_validId_returnsException() = runTest {
        // Act
        val result = userRepository.getUserById("user_99")

        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Usuario user_99 no encontrado")
    }
}