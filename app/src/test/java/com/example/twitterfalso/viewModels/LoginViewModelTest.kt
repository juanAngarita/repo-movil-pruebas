package com.example.twitterfalso.viewModels

import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.ui.Screens.login.LoginViewModel
import com.google.common.truth.Truth.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertNotEquals

class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repo: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)   // 👈 importante
        repo = mockk()
        viewModel = LoginViewModel(repo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando updateEmail es llamado, el estado cambia con el nuevo email`() {
        viewModel.updateEmail("juan@email.com")

        assertThat( viewModel.uiState.value.email).isEqualTo("juan@email.com")
    }

    @Test
    fun `mostrarEsconderPassword cambia de true a false y viceversa`() {
        val repo: AuthRepository = mockk()
        val viewModel = LoginViewModel(repo)

        val inicial = viewModel.uiState.value.mostrarContrasena
        viewModel.mostrarEsconderPassword()
        val cambiado = viewModel.uiState.value.mostrarContrasena

        assertNotEquals(inicial, cambiado)
    }

    @Test
    fun `cuando email o password están vacíos, muestra error obligatorio`() = runTest {
        val repo: AuthRepository = mockk()
        val viewModel = LoginViewModel(repo)

        viewModel.updateEmail("")
        viewModel.updatePassword("")
        viewModel.registerButtonPressed()

        val state = viewModel.uiState.value
        assertThat(state.mostrarMensaje).isTrue()
        assertThat(state.errorMessage).isEqualTo("Todos los campos son obligatorios")
        assertThat(state.navigate).isEqualTo(false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `cuando repo devuelve success, el estado navega a true`() = runTest {
        // 4. Configurar mock
        coEvery { repo.signIn("juan@email.com", "123456") } returns Result.success(Unit)

        // 5. Simular acciones
        viewModel.updateEmail("juan@email.com")
        viewModel.updatePassword("123456")
        viewModel.registerButtonPressed()
        advanceUntilIdle() // Aseguramos que la corrutina haya acabado para seguir con el codigo

        // 6. Verificar estado final
        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.errorMessage).isEqualTo("")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `cuando repo devuelve failure con mensaje, el estado refleja el error`() = runTest {
        coEvery { repo.signIn(any(), any()) } returns Result.failure(Exception("Credenciales incorrectas"))

        viewModel.updateEmail("test@email.com")
        viewModel.updatePassword("123456")
        viewModel.registerButtonPressed()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.errorMessage).isEqualTo("Credenciales incorrectas")
        assertThat(state.navigate).isFalse()
    }


}