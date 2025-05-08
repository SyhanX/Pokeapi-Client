package com.syhan.pokeapiclient.common.presentation.pokemon_list

import com.syhan.pokeapiclient.common.presentation.pokemon_details.PokemonShortDetailsState

data class PokemonListState(
    val list: List<PokemonShortDetailsState> = emptyList()
)

