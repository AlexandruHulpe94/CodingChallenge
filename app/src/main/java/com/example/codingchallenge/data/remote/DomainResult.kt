package com.example.codingchallenge.data.remote

sealed class DomainResult<DomainT> {
    data class Success<DomainT>(val value: DomainT) : DomainResult<DomainT>()
    data class Failure<DomainT>(val failureReason: DomainResultFailureReason) :
        DomainResult<DomainT>()
}

