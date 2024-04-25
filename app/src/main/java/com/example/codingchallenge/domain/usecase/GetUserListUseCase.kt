package com.example.codingchallenge.domain.usecase

import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.data.repository.UserRepository
import com.example.codingchallenge.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Flow<DomainResult<List<User>>> {
        return repository.getUsers().map { userListResult ->
            when (userListResult) {
                is DomainResult.Success -> {
                    DomainResult.Success(userListResult.value)
                }
                is DomainResult.Failure -> {
                    DomainResult.Failure(userListResult.failureReason)
                }
            }
        }
    }
}