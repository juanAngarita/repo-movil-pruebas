package com.example.twitterfalso.viewModels

import android.util.Log
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.Screens.login.LoginViewModel
import com.example.twitterfalso.ui.Screens.register.RegisterViewModel
import com.example.twitterfalso.ui.functions.Utils
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterViewModelUnitTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)   // 👈 importante
        authRepository = mockk()
        userRepository = mockk()
        viewModel = RegisterViewModel(authRepository, userRepository, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_success_createsUserAndUpdatesUiState() = runTest {
        // arrange
        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        coEvery {
            authRepository.signUp(any(), any())
        } returns Result.success(Unit)

        coEvery {
            userRepository.registerUser(any(), any(), any(), any(), any())
        } returns Result.success(Unit)

        mockkObject(Utils)
        coEvery { Utils.getCurrentUserId() } returns "fakeUserId123"

        viewModel.registerButtonPressed()
        advanceUntilIdle()
        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_alreadyUsedEmail_showsErrorMessage() = runTest {
        // arrange
        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        coEvery {
            authRepository.signUp(any(), any())
        } returns Result.failure(Exception("El correo ya está en uso"))

        viewModel.registerButtonPressed()
        advanceUntilIdle()

        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
        assertThat(state.errorMessage).isEqualTo("El correo ya está en uso")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_invalidEmail_showErrorMessage() = runTest {
        // arrange
        viewModel.updateEmail("testtest.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        coEvery {
            authRepository.signUp(any(), any())
        } returns Result.failure(Exception("El correo electrónico ya está en uso"))

        viewModel.registerButtonPressed()
        advanceUntilIdle()

        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}