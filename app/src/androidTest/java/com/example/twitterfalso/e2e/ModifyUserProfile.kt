package com.example.twitterfalso.e2e

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.twitterfalso.MainActivity
import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.Screens.updateProfile.UpdateProfileScreen
import com.example.twitterfalso.ui.Screens.updateProfile.UpdateProfileState
import com.example.twitterfalso.ui.Screens.updateProfile.UpdateProfileViewModel
import com.example.twitterfalso.ui.functions.Utils
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ModifyUserProfile {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @MockK
    lateinit var viewModel: UpdateProfileViewModel

    @Test
    fun updateUserInfo_correctInfo(){
        viewModel = mockk<UpdateProfileViewModel>()

        val fakeState = MutableStateFlow(
            UpdateProfileState(
                    name = "prueba",
                    location = "prueba",
                    bio = "prueba"
            )
        )

        every { viewModel.uistate } returns fakeState.asStateFlow()

        composeRule.setContent {
            UpdateProfileScreen(
                viewModel
            )
        }

        Thread.sleep(1000) // 3 segundos para ver la pantalla
        composeRule.onNodeWithTag("nameField").assertExists()
        composeRule.onNodeWithTag("nameField").assert(hasText("prueba"))
    }
}