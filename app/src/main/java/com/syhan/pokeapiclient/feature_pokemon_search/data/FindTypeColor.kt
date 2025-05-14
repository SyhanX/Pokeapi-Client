package com.syhan.pokeapiclient.feature_pokemon_search.data

import androidx.compose.ui.graphics.Color

fun findTypeColor(typeName: String): Color {
    val colorHexCode = PokemonTypeColor.entries.find {
        it.name == typeName
    }?.color

    return Color(colorHexCode ?: PokemonTypeColor.Unknown.color)
}