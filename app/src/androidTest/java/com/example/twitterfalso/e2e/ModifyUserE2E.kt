package com.example.twitterfalso.e2e

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.twitterfalso.MainActivity
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.local.SeedHelper
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.Screens.updateProfile.UpdateProfileScreen
import com.example.twitterfalso.ui.functions.Utils
import com.example.twitterfalso.ui.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ModifyUserE2E {

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
            authRepository.signUp("prueba@prueba.com", "123456")
            authRepository.signIn("prueba@prueba.com", "123456")

            val userId = Utils.getCurrentUserId()

            userRepository.registerUser(
                "nuevo",
                "Col",
                "Nuevo",
                "",
                userId
            )
        }
    }

    @Test
    fun updateUserInfo_InfoUpdated(){
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("homeScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription(Screen.Profile.route).performClick()

        composeRule.onNodeWithTag("nameField").isDisplayed()
        composeRule.onNodeWithTag("nameField").performTextClearance()
        composeRule.onNodeWithTag("nameField").performTextInput("Modificado")

        composeRule.onNodeWithTag("btnUpdateProfile").performClick()

        composeRule.onNodeWithContentDescription(Screen.Home.route).performClick()

        composeRule.onNodeWithContentDescription(Screen.Profile.route).performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("nameField").fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(3000)
        composeRule.onNodeWithTag("nameField").assertTextContains("Modificado", substring = true)
    }

    @After
    fun after(){
        authRepository.signOut()
    }
}