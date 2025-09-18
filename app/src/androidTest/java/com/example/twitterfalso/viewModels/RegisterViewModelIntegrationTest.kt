package com.example.twitterfalso.viewModels

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.injection.IoDispatcher
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.Screens.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterViewModelIntegrationTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099) // auth emulator
            Firebase.firestore.useEmulator("10.0.2.2", 8080) // firestore emulator
        }catch (e: Exception){

        }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)
        userRepository = UserRepository(userDatasource)
        authRepository = AuthRepository(authRemoteDataSource,userRepository)

    }

    //opcion 1. Esperamos a que estados cambien,
    //esto dado que estamos esperando tiempo real de conexion
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_success_createsUserAndUpdatesUiState() = runTest {
        viewModel = RegisterViewModel(authRepository, userRepository)
        // arrange
        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        viewModel.registerButtonPressed()

        // Esto suspende hasta que navigate sea false
        val navigate = viewModel.uistate
            .map { it.navigate }
            .first { it }
        assertThat(navigate).isTrue()
        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    //opcion 2. Pasar el el dispatcher
    //Al pasar el dispatcher se deberian ejecutar todas las corrutinas
    //con advanceUntilIdle se deberia esperar a que se complete la corrutina
    //Pero no funciona
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_success_createsUserAndUpdatesUiState2() = runTest{
        val testScheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(testScheduler)

        Dispatchers.setMain(dispatcher);
        viewModel = RegisterViewModel(authRepository, userRepository,dispatcher)
        // arrange
        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        viewModel.registerButtonPressed()

        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }

    //Opcion 3 cuando probamos directamente nuestras funciones suspend
    //sin pasar por viewModel.launch al parecer si funciona
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_success_createsUserAndUpdatesUiState3() = runTest{
        val testScheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(testScheduler)

        Dispatchers.setMain(dispatcher);
        viewModel = RegisterViewModel(authRepository, userRepository,dispatcher)
        // arrange
        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        viewModel.registerUserOnline()

        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEmpty()
    }


    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun register_alreadyUsedEmail_showsErrorMessage() = runTest {
        // arrange
        authRepository.signUp("test@test.com", "123456")

        viewModel = RegisterViewModel(authRepository, userRepository, )
        viewModel.updateEmail("test@test.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        viewModel.registerButtonPressed()

        Log.d("TAG", "register_alreadyUsedEmail_showsErrorMessage: ${viewModel.uistate.value.errorMessage}")

        // Esto suspende hasta que navigate sea true
        var loadingValue = viewModel.uistate
            .map { it.loading }
            .first { it }
        assertThat(loadingValue).isTrue()

        // Esto suspende hasta que navigate sea false
        loadingValue = viewModel.uistate
            .map { it.loading }
            .first { !it }
        assertThat(loadingValue).isFalse()

        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
        assertThat(state.errorMessage).isEqualTo("El correo electrónico ya está en uso")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun register_invalidEmail_showErrorMessage() = runTest {
        viewModel = RegisterViewModel(authRepository, userRepository )
        // arrange
        viewModel.updateEmail("testtest.com")
        viewModel.updatePassword("123456")
        viewModel.updateName("Juan")
        viewModel.updateUserName("juanito")
        viewModel.updatePais("CO")

        viewModel.registerButtonPressed()

        // Esto suspende hasta que navigate sea true
        var loadingValue = viewModel.uistate
            .map { it.loading }
            .first { it }
        assertThat(loadingValue).isTrue()

        // Esto suspende hasta que navigate sea false
        loadingValue = viewModel.uistate
            .map { it.loading }
            .first { !it }
        assertThat(loadingValue).isFalse()

        // assert
        val state = viewModel.uistate.value
        assertThat(state.navigate).isFalse()
        assertThat(state.errorMessage).isNotEmpty()
        assertThat(state.errorMessage).isEqualTo("El correo electrónico no es válido")
    }

    @After
    fun finish() = runTest {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val user = Firebase.auth.currentUser
        user?.delete()?.await()
        Firebase.auth.signOut()
    }
}