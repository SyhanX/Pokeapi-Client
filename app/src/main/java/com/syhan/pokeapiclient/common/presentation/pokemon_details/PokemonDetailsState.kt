package com.syhan.pokeapiclient.common.presentation.pokemon_details

import com.syhan.pokeapiclient.common.domain.model.Sprites

data class PokemonDetailsState(
    val name: String = "",
    val sprites: Sprites = Sprites(frontDefault = null),
)
