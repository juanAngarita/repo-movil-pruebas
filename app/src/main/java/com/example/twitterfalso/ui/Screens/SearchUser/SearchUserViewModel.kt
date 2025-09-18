package com.example.twitterfalso.ui.Screens.SearchUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val _uistate = MutableStateFlow(SearchUserState())
    val uistate: StateFlow<SearchUserState> = _uistate



    fun observeQuery(searchQueryFlow: Flow<String>) {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(500)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    flow {
                        emit(userRepository.searchUsers(query)) // Result<List<UserProfileInfo>>
                    }
                }
                .collect { result ->
                    val users = result.getOrElse { emptyList() }
                    _uistate.update { it.copy(searchQuery = "", users = users) }
                }
        }
    }

}