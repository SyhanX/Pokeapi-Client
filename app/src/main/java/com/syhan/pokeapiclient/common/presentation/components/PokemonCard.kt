package com.syhan.pokeapiclient.common.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme

private const val TAG = "PokemonCard"
@Composable
fun PokemonCard(
    name: String,
    sprite: String,
) {
    Log.d(TAG, "PokemonCard: $name and \n $sprite")
    Card {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            AsyncImage(
                model = sprite,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                onError = {
                    Log.d(TAG, "PokemonCard: there has been an error")
                },
                modifier = Modifier
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            Color.LightGray
                        } else {
                            Color.White
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .size(150.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PokemonCardPreview() {
    PokeapiClientTheme {
        PokemonCard(
            name = "Pinkachu",
            sprite = "3",
        )
    }
}