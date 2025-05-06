package com.syhan.pokeapiclient.common.presentation.components

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme

@Composable
fun PokemonTypeTag(type: String) {
    TextButton(
        onClick = {},
        enabled = false
    ) {
        Text(
            text = type,
            fontSize = 18.sp
        )
    }
}


@PreviewLightDark
@Composable
private fun TagPreview() {
    PokeapiClientTheme {
        Surface {
            PokemonTypeTag("Poison")
        }
    }
}