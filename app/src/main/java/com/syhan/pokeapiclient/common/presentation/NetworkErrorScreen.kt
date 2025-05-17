package com.syhan.pokeapiclient.common.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.common.domain.NetworkErrorType

@Composable
fun NetworkErrorScreen(
    errorType: NetworkErrorType,
    onRetry: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_sad_face),
            contentDescription = stringResource(R.string.no_internet_description),
            modifier = Modifier
                .size(100.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            fontSize = 20.sp,
            text = stringResource(
                when (errorType) {
                    NetworkErrorType.NoInternet -> {
                        R.string.no_internet
                    }
                    NetworkErrorType.UnexpectedHttpResponse -> {
                        R.string.unexpected_error
                    }
                }
            )
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(
                text = stringResource(R.string.try_again),
                fontSize = 18.sp
            )
        }
    }
}