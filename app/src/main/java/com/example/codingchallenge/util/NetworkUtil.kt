package com.example.codingchallenge.util

import com.example.codingchallenge.data.remote.NetworkResult
import com.example.codingchallenge.data.remote.NetworkResultFailureReason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> remoteCall(func: suspend () -> Response<T>): NetworkResult<T> = withContext(
    Dispatchers.IO
) {
    try {
        val result = func()
        result.toNetworkResult()
    } catch (e: IOException) {
        mapExceptionToNetworkResult(e)
    }
}

private fun <T> mapExceptionToNetworkResult(e: Exception): NetworkResult<T> =
    NetworkResult.Failure(
        when (e) {
            is SocketTimeoutException -> NetworkResultFailureReason.SocketTimeout
            else -> NetworkResultFailureReason.Unknown(e)
        }
    )

private fun <T> Response<T>.toNetworkResult(): NetworkResult<T> =
    if (this.isSuccessful) NetworkResult.Success(this.body()!!)
    else {
        NetworkResult.Failure(
            when (this.code()) {
                400 -> NetworkResultFailureReason.BadRequest
                401 -> NetworkResultFailureReason.Unauthorized
                403 -> NetworkResultFailureReason.Forbidden
                404 -> NetworkResultFailureReason.NotFound
                500 -> NetworkResultFailureReason.InternalError
                else -> NetworkResultFailureReason.Unknown(null)
            }
        )
    }