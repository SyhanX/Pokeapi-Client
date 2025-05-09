package com.syhan.pokeapiclient.common.domain.util

import android.util.Log
import com.syhan.pokeapiclient.common.data.NetworkErrorType
import com.syhan.pokeapiclient.common.data.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

private const val TAG = "DoNetworkRequest"

fun doSimpleNetworkRequest(
    networkState: MutableStateFlow<NetworkResponse>,
    scope: CoroutineScope,
    request: suspend () -> Unit
) {
    scope.launch(Dispatchers.IO) {
        networkState.value = NetworkResponse.Loading
        Log.i(TAG, "Trying to load")
        try {
            request()
            networkState.value = NetworkResponse.Success
            Log.i(TAG, "Loaded Successfully")
        } catch (e: IOException) {
            networkState.value = NetworkResponse.Error(NetworkErrorType.NoInternetError)
            Log.e(TAG, "No internet\n $e")
        } catch (e: HttpException) {
            networkState.value = NetworkResponse.Error(NetworkErrorType.UnexpectedNetworkError)
            Log.e(TAG, "http is cracked lmaooo\n $e")
        } catch (e: Exception) {
            networkState.value = NetworkResponse.Error(NetworkErrorType.UnknownError)
            Log.e(TAG, "Unexpected Exception\n $e")
        }
    }
}