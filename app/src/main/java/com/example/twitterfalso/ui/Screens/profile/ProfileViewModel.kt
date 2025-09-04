package com.example.twitterfalso.ui.Screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.StorageRepository
import com.example.twitterfalso.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(
        ProfileState(
        email = authRepository.currentUser?.email ?: "",
        profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
    )
    )
    val state: StateFlow<ProfileState> = _state

    fun logout(){
        authRepository.signOut()

    }
    fun uploadImageToFirebase(uri: Uri){
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if(result.isSuccess){
                _state.update { it.copy(profileImageUrl = result.getOrNull()) }

                val photoURL = result.getOrNull()
                if(photoURL != null){
                    val userId = authRepository.currentUser?.uid ?: return@launch
                    userRepository.updateUserProfileImage(userId = userId, photoUrl = photoURL)
                }

            }
        }
    }

}