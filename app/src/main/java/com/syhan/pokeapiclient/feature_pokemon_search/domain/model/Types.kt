package com.syhan.pokeapiclient.feature_pokemon_search.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Type(
    val slot: Int,
    val type: TypeInfo
)

@Immutable
@Serializable
data class TypeInfo(
    val name: String,
    val url: String
)