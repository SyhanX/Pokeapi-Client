package com.syhan.pokeapiclient.feature_pokemon_search.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Stat(
    @SerialName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatInfo
)

@Immutable
@Serializable
data class StatInfo(
    val name: String,
    val url: String
)