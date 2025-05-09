package com.syhan.pokeapiclient.common.domain

sealed interface NetworkResponse {
    data object Loading: NetworkResponse
    data class Error(val type: NetworkErrorType): NetworkResponse
    data object Success: NetworkResponse
}

enum class NetworkErrorType {
    NoInternetError,
    UnexpectedNetworkError,
    UnknownError
}