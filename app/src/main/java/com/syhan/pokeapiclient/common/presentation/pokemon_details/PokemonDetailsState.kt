package com.syhan.pokeapiclient.common.presentation.pokemon_details

import com.syhan.pokeapiclient.common.domain.model.Sprites
import com.syhan.pokeapiclient.common.domain.model.Stat
import com.syhan.pokeapiclient.common.domain.model.Type

data class PokemonShortDetailsState(
    val id: Int = -1,
    val name: String = "",
    val sprites: Sprites = Sprites(
        frontDefault = null,
        frontShiny = null
    ),
)

data class PokemonFullDetailsState(
    val id: Int = -1,
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<Type> = emptyList(),
    val stats: List<Stat> = emptyList(),
    val sprites: Sprites = Sprites(
        frontDefault = null,
        frontShiny = null
    ),
)
