package com.syhan.pokeapiclient.common.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class PokemonShortInfo(
    val id: Int,
    val name: String,
    val sprites: Sprites,
)

@Immutable
@Serializable
data class PokemonFullInfo(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: Types,
    val stats: Stats,
    val sprites: String,
)