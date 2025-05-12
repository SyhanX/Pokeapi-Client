package com.syhan.pokeapiclient.common.domain

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import okio.IOException
import retrofit2.HttpException

private const val TAG = "NetworkResponse"

sealed interface NetworkResponse {
    data object InitialLoading : NetworkResponse
    data class Error(val type: NetworkErrorType) : NetworkResponse
    data object Success : NetworkResponse
}

enum class NetworkErrorType {
    NoInternetError,
    UnexpectedNetworkError,
    UnknownError
}

fun MutableStateFlow<NetworkResponse>.setInitialLoading() {
    Log.i(TAG, "Loading for the first time")
    this.value = NetworkResponse.InitialLoading
}

fun MutableStateFlow<NetworkResponse>.setSuccess() {
    Log.i(TAG, "Success")
    this.value = NetworkResponse.Success
}

fun MutableStateFlow<NetworkResponse>.setIoException(e: IOException) {
    Log.e(TAG, "No internet connection\n${e.message}")
    this.value = NetworkResponse.Error(NetworkErrorType.NoInternetError)
}

fun MutableStateFlow<NetworkResponse>.setHttpException(e: HttpException) {
    Log.e(TAG, "Http is cracked lmao\n${e.message}")
    this.value = NetworkResponse.Error(NetworkErrorType.UnexpectedNetworkError)
}

fun MutableStateFlow<NetworkResponse>.setUnknownException(e: Exception) {
    Log.e(TAG, "An unknown exception has occurred\n${e.message}")
    this.value = NetworkResponse.Error(NetworkErrorType.UnknownError)
}