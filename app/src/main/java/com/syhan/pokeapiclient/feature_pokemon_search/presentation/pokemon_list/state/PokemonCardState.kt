package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list.state

import androidx.compose.runtime.Immutable
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Sprites
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type

@Immutable
data class PokemonCardState(
    val id: Int = -1,
    val name: String = "",
    val sprites: Sprites = Sprites(),
    val types: List<Type> = emptyList(),
    val hpValue: Int,
    val attackValue: Int,
    val defenseValue: Int,
)