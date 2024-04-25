package com.example.codingchallenge.presentation.state

import com.example.codingchallenge.domain.model.User

data class UserViewState(
    val userList: List<User> = emptyList(),
    val userDetail: User? = null,
    val error: String? = null
)

sealed class UserIntent {
    object FetchUsers : UserIntent()
    data class FetchUserDetail(val userId: Int) : UserIntent()
}