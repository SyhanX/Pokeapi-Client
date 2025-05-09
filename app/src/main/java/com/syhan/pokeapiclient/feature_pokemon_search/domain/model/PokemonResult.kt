package com.syhan.pokeapiclient.feature_pokemon_search.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable


@Immutable
@Serializable
data class PokemonResult(
    val name: String,
    val url: String,
)

@Immutable
@Serializable
data class PokemonResultList(
    val results: List<PokemonResult>
)
