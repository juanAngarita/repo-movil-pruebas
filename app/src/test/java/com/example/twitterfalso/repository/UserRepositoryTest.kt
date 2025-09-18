package com.example.twitterfalso.repository

import com.example.twitterfalso.data.datasource.UserRemoteDataSource
import com.example.twitterfalso.data.dtos.UserProfileFirestoreDto
import com.example.twitterfalso.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserRepositoryTest {
    private val mockDataSource = mockk<UserRemoteDataSource>()
    private val repository = UserRepository(mockDataSource)

    @Test
    fun `al llamar getUserById, si el id es valido retorna un Result con el userInfo`() = runTest {
        // Arrange
        val dto = UserProfileFirestoreDto(
            id = "123",
            username = "juan",
            email = "juan@email",
            name = "Juan",
            bio = "Bio",
            pais = "Argentina",
            website = "www.google.com",
            profileImage = "www.google.com",
            coverImage = "www.google.com",
            birthDate = "2000-01-01",
            verified = false,
            followersCount = 0,
            followingCount = 0,
            createdAt = "2023-01-01",
            updatedAt = "2023-01-01"
        )
        coEvery { mockDataSource.getUserById("123") } returns dto
        // Act
        val result = repository.getUserById("123")
        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("123")
        Truth.assertThat(result.getOrNull()?.name).isEqualTo("Juan")
    }

    @Test
    fun `al llamar getUserById, si el dataSource lanza excepcion retorna un Result failure`() =
        runTest {
            // Arrange
            val exception = RuntimeException("User not found")
            coEvery { mockDataSource.getUserById("999") } throws exception

            // Act
            val result = repository.getUserById("999")

            // Assert
            Truth.assertThat(result.isFailure).isTrue()
            Truth.assertThat(result.exceptionOrNull()).isEqualTo(exception)
        }

    @Test
    fun `al llamar getUserById, si el dataSource devuelve campos vacios se mapean correctamente`() =
        runTest {
            // Arrange
            val dto = UserProfileFirestoreDto(
                id = "456",
                username = "",
                email = "",
                name = "",
                bio = null,
                pais = null,
                website = null,
                profileImage = "",
                coverImage = "",
                birthDate = "1990-01-01",
                verified = false,
                followersCount = 0,
                followingCount = 0,
                createdAt = "2023-01-01",
                updatedAt = "2023-01-01"
            )
            coEvery { mockDataSource.getUserById("456") } returns dto

            // Act
            val result = repository.getUserById("456")

            // Assert
            Truth.assertThat(result.isSuccess).isTrue()
            Truth.assertThat(result.getOrNull()?.bio).isEqualTo("No hay biografia")
            Truth.assertThat(result.getOrNull()?.location).isEqualTo("No hay ubicación")
            Truth.assertThat(result.getOrNull()?.website).isEqualTo("")
        }

    @Test
    fun `al llamar getUserById, si el usuario no existe retorna failure`() = runTest {
        // Arrange
        val userId = "9999"
        coEvery { mockDataSource.getUserById(userId) } throws NoSuchElementException("Usuario $userId no encontrado")

        // Act
        val result = repository.getUserById(userId)

        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(NoSuchElementException::class.java)
        //Mensaje de la excepcion
        Truth.assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("Usuario $userId no encontrado")
    }

    @Test
    fun `al llamar getUserById, si currentUserId sigue al usuario entonces followed es true`() =
        runTest {
            // Arrange
            val dto = UserProfileFirestoreDto(
                id = "123",
                username = "juan",
                email = "juan@email",
                name = "Juan",
                bio = "Bio",
                pais = "Argentina",
                website = "www.google.com",
                profileImage = "www.google.com",
                coverImage = "www.google.com",
                birthDate = "2000-01-01",
                verified = false,
                followersCount = 10,
                followingCount = 5,
                createdAt = "2023-01-01",
                updatedAt = "2023-01-01",
            ).apply { followed = true } // 🔹 simular que Firestore encontró al currentUserId

            coEvery { mockDataSource.getUserById("123", "999") } returns dto

            // Act
            val result = repository.getUserById("123", "999")

            // Assert
            Truth.assertThat(result.isSuccess).isTrue()
            Truth.assertThat(result.getOrNull()?.followed).isTrue()
        }

}