package com.example.codingchallenge.data.remote

sealed class DomainResultFailureReason {
    data class NetworkError(val message: String) : DomainResultFailureReason()
    data class Unavailable(val message: String) : DomainResultFailureReason()
    data class InternalError(val message: String) : DomainResultFailureReason()
    data class Unknown(val exception: Exception?) : DomainResultFailureReason()
}
