package com.syhan.pokeapiclient.common.domain

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import okio.IOException

private const val TAG = "NetworkResponse"

sealed interface NetworkRequestState {
    data object Loading : NetworkRequestState
    data class Error(val type: NetworkErrorType) : NetworkRequestState
    data object Success : NetworkRequestState
}

sealed interface NetworkErrorType {
    data object NoInternet : NetworkErrorType
    data object UnexpectedHttpResponse : NetworkErrorType
}

object NetworkRequestStateHandler {
    fun MutableStateFlow<NetworkRequestState>.setLoading() {
        Log.i(TAG, "Loading data")
        this.value = NetworkRequestState.Loading
    }

    fun MutableStateFlow<NetworkRequestState>.setSuccess() {
        Log.i(TAG, "Data loaded successfully")
        this.value = NetworkRequestState.Success
    }

    fun MutableStateFlow<NetworkRequestState>.setError(exception: Exception) {
        Log.e(TAG, exception.message.toString())
        this.value = NetworkRequestState.Error(
            if (exception is IOException) NetworkErrorType.NoInternet
            else NetworkErrorType.UnexpectedHttpResponse
        )
    }
}
