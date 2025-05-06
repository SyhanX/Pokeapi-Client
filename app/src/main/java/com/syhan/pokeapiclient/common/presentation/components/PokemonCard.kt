package com.syhan.pokeapiclient.common.presentation.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PokemonCard(
    name: String,
    image: String,
    types: List<String>
) {
    Card {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                error = painterResource(R.drawable.pinkachu),
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


@OptIn(ExperimentalCoilApi::class)
@PreviewLightDark
@Composable
private fun PokemonCardPreview() {
    PokeapiClientTheme {
        PokemonCard(
            name = "Pinkachu",
            image = "3",
            types = emptyList()
        )
    }
}