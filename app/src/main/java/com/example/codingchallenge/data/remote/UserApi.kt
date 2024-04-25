package com.example.codingchallenge.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users")
    suspend fun getUsers(): Response<UserApiResponse>

    @GET("users/{id}")
    suspend fun getUserDetail(@Path("id") userId: Int): Response<NetworkUserDetailResponse>
}