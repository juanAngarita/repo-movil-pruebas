package com.example.twitterfalso.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.twitterfalso.MainActivity
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.twitterfalso.data.local.SeedHelper

@HiltAndroidTest
class RegisterNewUserE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        hiltRule.inject()
        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099) // auth emulator
            Firebase.firestore.useEmulator("10.0.2.2", 8080) // firestore emulator
        }catch (e: Exception){

        }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userDatasource = UserFirestoreDataSourceImpl(Firebase.firestore)
        userRepository = UserRepository(userDatasource)
        authRepository = AuthRepository(authRemoteDataSource,userRepository)
        runBlocking {
            authRepository.signUp("admin@admin.com", "123456")
            authRepository.signOut()
            SeedHelper.seedUsersAndTweets(Firebase.firestore)
        }
    }

    @Test
    fun navigate_fromStart_toLogin() {
        // Click en el botón de login
        composeRule.onNodeWithTag("btnLogin").performClick()

        // Verifica que la pantalla de Login aparece
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
    }

    @Test
    fun navigate_fromStart_toRegister() {
        // Click en el botón de register
        composeRule.onNodeWithTag("btnRegister").performClick()

        // Verifica que la pantalla de Register aparece
        composeRule.onNodeWithTag("registerScreen").assertIsDisplayed()
    }

    @Test
    fun registerUser_shortPassword_showMessage() {
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.onNodeWithTag("nameField").performTextInput("Juan Sebastian")

        composeRule.onNodeWithTag("userNameField").performTextInput("Juan Sebastian")

        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")

        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")

        composeRule.onNodeWithTag("emailField").performTextInput("admin@admin.com")

        composeRule.onNodeWithTag("passwordField").performTextInput("12345")

        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.onNodeWithTag("errorMessage").assertIsDisplayed()

    }

    @Test
    fun registerUser_allValidsInputs_navigateHome() {
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.onNodeWithTag("nameField").performTextInput("Juan Sebastian")

        composeRule.onNodeWithTag("userNameField").performTextInput("Juan Sebastian")

        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")

        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")

        composeRule.onNodeWithTag("emailField").performTextInput("juan@admin.com")

        composeRule.onNodeWithTag("passwordField").performTextInput("123456")

        composeRule.onNodeWithTag("btnRegister").performClick()

        // espera a que navegue (si tienes animaciones, dale un poco de tiempo)
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("homeScreen").assertIsDisplayed()
    }

    @Test
    fun registerUser_usedEmail_showMessage() {
        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.onNodeWithTag("nameField").performTextInput("Juan Sebastian")

        composeRule.onNodeWithTag("userNameField").performTextInput("Juan Sebastian")

        composeRule.onNodeWithTag("paisField").performTextInput("Colombia")

        composeRule.onNodeWithTag("bioField").performTextInput("Estudiante")

        composeRule.onNodeWithTag("emailField").performTextInput("admin@admin.com")

        composeRule.onNodeWithTag("passwordField").performTextInput("123456")

        composeRule.onNodeWithTag("btnRegister").performClick()

        composeRule.onNodeWithTag("errorMessage").assertIsDisplayed()
    }

    @After
    fun cleanDatabase() = runTest{
        val user = Firebase.auth.currentUser
        user?.delete()?.await()
        Firebase.auth.signOut()
    }




}