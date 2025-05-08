package com.syhan.pokeapiclient.common.presentation.pokemon_details

import com.syhan.pokeapiclient.common.domain.model.Sprites
import com.syhan.pokeapiclient.common.domain.model.Stats
import com.syhan.pokeapiclient.common.domain.model.Types

data class PokemonShortDetailsState(
    val id: Int = 0,
    val name: String = "",
    val sprites: Sprites = Sprites(frontDefault = null),
)

data class PokemonFullDetailsState(
    val id: Int = 0,
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val types: Types = Types(
        types = emptyList()
    ),
    val stats: Stats = Stats(
        stats = emptyList()
    ),
    val sprites: Sprites = Sprites(
        frontDefault = null
    ),
)
