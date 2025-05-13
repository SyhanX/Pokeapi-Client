package com.syhan.pokeapiclient.feature_pokemon_search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import com.syhan.pokeapiclient.feature_pokemon_search.data.PokemonTypeColor

@Composable
fun PokemonTypeTag(
    name: String,
    color: Color,
) {
    Box(
        modifier = Modifier
            .background(
                color = color,
                shape = RoundedCornerShape(30)
            )
    ) {
        Text(
            text = name,
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}


@Preview
@Composable
private fun TagPreview() {
    PokeapiClientTheme {
        Column {
            val types = PokemonTypeColor.entries
            types.forEach {
                PokemonTypeTag(it.name, Color(it.color))
            }
        }
    }
}