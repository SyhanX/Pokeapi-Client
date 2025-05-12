package com.syhan.pokeapiclient.feature_pokemon_search.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class PokemonDetailsShort(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<Type>
)

@Immutable
@Serializable
data class PokemonDetailsFull(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
)