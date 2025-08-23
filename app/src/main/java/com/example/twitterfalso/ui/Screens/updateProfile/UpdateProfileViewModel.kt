package com.example.twitterfalso.ui.Screens.updateProfile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.dtos.UpdateUserDto
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.StorageRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.functions.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    init{
        val userId = Utils.getCurrentUserId()
        viewModelScope.launch {
            val result = userRepository.getUserById(userId)
            if(result.isSuccess){
                val user = result.getOrNull()
                if (user != null) {
                    _uistate.update {
                        it.copy(
                            name = user.name,
                            bio = user.bio,
                            location = user.location,
                            profileImage = user.profileImage?:"",
                            backgroundImage = user.backgroundImage?:""
                        )
                    }
                }
            }
        }
    }

    val _uistate = MutableStateFlow(UpdateProfileState())
    val uistate: StateFlow<UpdateProfileState> = _uistate

    fun updateProfile(){
        val userId = Utils.getCurrentUserId()
        val updateUserDto = UpdateUserDto(
            name = _uistate.value.name,
            bio = _uistate.value.bio,
            pais = _uistate.value.location
        )
        viewModelScope.launch {
            userRepository.updateUserProfile(userId, updateUserDto)
        }

    }

    fun uploadProfileImageToFirebase(uri: Uri){
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if(result.isSuccess){
                _uistate.update { it.copy(profileImage = result.getOrNull() ?: "") }
                val photoURL = result.getOrNull()
                if(photoURL != null){
                    val userId = Utils.getCurrentUserId()
                    userRepository.updateUserProfileImage(userId = userId, photoUrl = photoURL)
                }
            }
        }
    }

    fun uploadProfileBackgroundToFirebase(uri: Uri){
        viewModelScope.launch {
            val result = storageRepository.uploadBackgroundImage(uri)
            if(result.isSuccess){
                _uistate.update { it.copy(backgroundImage = result.getOrNull() ?: "") }
                val photoURL = result.getOrNull()
                if(photoURL != null){
                    val userId = Utils.getCurrentUserId()
                    userRepository.updateUserBackgroundImage(userId = userId, photoUrl = photoURL)
                }
            }
        }
    }

    fun updateName(input: String) {
        _uistate.update {
            it.copy(name = input)
        }
    }

    fun updateBio(input: String) {
        _uistate.update {
            it.copy(bio = input)
        }
    }

    fun updateLocation(input: String) {
        _uistate.update {
            it.copy(location = input)
        }
    }


}