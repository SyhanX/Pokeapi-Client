package com.syhan.pokeapiclient.common.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Pokemon(
    val name: String,
    val sprites: Sprites,
)

@Immutable
@Serializable
data class Sprites(
    @SerialName("front_default")
    val frontDefault: String? = null,
)

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
