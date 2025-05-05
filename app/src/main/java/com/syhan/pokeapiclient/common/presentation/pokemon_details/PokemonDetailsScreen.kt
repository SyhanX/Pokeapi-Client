package com.syhan.pokeapiclient.common.presentation.pokemon_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme

@Composable
fun PokemonDetailsScreen(
    onButtonClick: () -> Unit
) {
    PokemonDetailsContent {
        onButtonClick()
    }
}

@Composable
fun PokemonDetailsContent(
    onButtonClick: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Button(
                onClick = onButtonClick
            ) {
                Text(
                    text = "Navigate to list"
                )
            }
        }
    }
}

@Preview
@Composable
private fun PokemonDetailsPreview() {
    PokeapiClientTheme {
        PokemonDetailsContent {  }
    }
}