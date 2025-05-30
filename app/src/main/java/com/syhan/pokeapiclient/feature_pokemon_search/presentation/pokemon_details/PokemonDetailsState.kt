package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import androidx.compose.runtime.Immutable
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Sprites
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Stat
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type

@Immutable
data class PokemonDetailsState(
    val id: Int = -1,
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<Type> = emptyList(),
    val stats: List<Stat> = emptyList(),
    val sprites: Sprites = Sprites(),
)