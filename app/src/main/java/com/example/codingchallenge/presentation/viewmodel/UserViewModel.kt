package com.example.codingchallenge.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.domain.usecase.GetUserDetailUseCase
import com.example.codingchallenge.domain.usecase.GetUserListUseCase
import com.example.codingchallenge.presentation.state.UserIntent
import com.example.codingchallenge.presentation.state.UserViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    private val _intent = MutableStateFlow<UserIntent>(UserIntent.FetchUsers)
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UserViewState> = _intent.flatMapLatest { intent ->
        when (intent) {
            is UserIntent.FetchUsers -> getUserListUseCase()
                .map { users ->
                    when (users) {
                        is DomainResult.Success -> UserViewState(
                            userList = users.value,
                            error = null
                        )

                        is DomainResult.Failure -> UserViewState(
                            error = "Failed to load users"
                        )
                    }
                }

            is UserIntent.FetchUserDetail -> getUserDetailUseCase(intent.userId)
                .map { user ->
                    when (user) {
                        is DomainResult.Success -> UserViewState(
                            userDetail = user.value,
                            error = null
                        )

                        is DomainResult.Failure -> UserViewState(
                            error = "Failed to load user detail"
                        )
                    }
                }
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UserViewState())

    fun processIntent(intent: UserIntent) {
        _intent.value = intent
    }
}






