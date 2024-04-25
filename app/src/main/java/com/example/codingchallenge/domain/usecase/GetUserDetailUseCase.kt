package com.example.codingchallenge.domain.usecase

import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.data.repository.UserRepository
import com.example.codingchallenge.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Int): Flow<DomainResult<User?>> {
        return repository.getUserDetail(userId).map { userDetailResult ->
            when (userDetailResult) {
                is DomainResult.Success -> {
                    DomainResult.Success(userDetailResult.value)
                }
                is DomainResult.Failure -> {
                    DomainResult.Failure(userDetailResult.failureReason)
                }
            }
        }
    }
}

