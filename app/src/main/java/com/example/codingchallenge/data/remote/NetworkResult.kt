package com.example.codingchallenge.data.remote

sealed class NetworkResult<ResponseT> {
    data class Success<ResponseT>(val value: ResponseT) : NetworkResult<ResponseT>()
    data class Failure<ResponseT>(val failureReason: NetworkResultFailureReason) :
        NetworkResult<ResponseT>()
}

sealed class NetworkResultFailureReason {
    object Unauthorized : NetworkResultFailureReason()
    object NotFound : NetworkResultFailureReason()
    object InternalError : NetworkResultFailureReason()
    object Forbidden : NetworkResultFailureReason()
    object BadRequest : NetworkResultFailureReason()
    object SocketTimeout : NetworkResultFailureReason()
    class Unknown(val exception: Exception?) : NetworkResultFailureReason()
}