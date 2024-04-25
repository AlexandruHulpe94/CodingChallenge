package com.example.codingchallenge.data.repository

import com.example.codingchallenge.data.local.database.AppDatabase
import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.data.remote.DomainResultFailureReason
import com.example.codingchallenge.data.remote.NetworkResult
import com.example.codingchallenge.data.remote.UserApi
import com.example.codingchallenge.domain.model.User
import com.example.codingchallenge.mapper.UserMapper.toDatabaseUser
import com.example.codingchallenge.mapper.UserMapper.toDomainUser
import com.example.codingchallenge.util.remoteCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi,
    private val db: AppDatabase
) {
    suspend fun getUsers(): Flow<DomainResult<List<User>>> = flow {
        when (val networkUsers = remoteCall { api.getUsers() }) {
            is NetworkResult.Success -> {
                if (networkUsers.value.data.isNotEmpty()) {
                    withContext(Dispatchers.IO) {
                        db.userDao().insertAll(networkUsers.value.data.map { it.toDatabaseUser() })
                    }
                    emit(DomainResult.Success(networkUsers.value.data.map { it.toDomainUser() }))
                }
            }
            is NetworkResult.Failure -> {
                val databaseUsers = withContext(Dispatchers.IO) { db.userDao().getAll() }
                if (databaseUsers.isNotEmpty()) {
                    emit(DomainResult.Success(databaseUsers.map { it.toDomainUser() }))
                } else {
                    emit(DomainResult.Failure(DomainResultFailureReason.Unknown(null)))
                }
            }
        }
    }


    suspend fun getUserDetail(userId: Int): Flow<DomainResult<User>> = flow {
        when (val networkUser = remoteCall { api.getUserDetail(userId) }) {
            is NetworkResult.Success -> {
                val user = networkUser.value
                withContext(Dispatchers.IO) {
                    db.userDao().insert(user.data.toDatabaseUser())
                }
                emit(DomainResult.Success(user.data.toDomainUser()))
            }
            is NetworkResult.Failure -> {
                val databaseUser = withContext(Dispatchers.IO) { db.userDao().getUser(userId) }
                if (databaseUser != null) {
                    emit(DomainResult.Success(databaseUser.toDomainUser()))
                } else {
                    emit(DomainResult.Failure(DomainResultFailureReason.Unknown(null)))
                }
            }
        }
    }

}
