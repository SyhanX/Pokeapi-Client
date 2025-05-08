package com.syhan.pokeapiclient.common.presentation.pokemon_list

import com.syhan.pokeapiclient.common.presentation.pokemon_details.PokemonDetailsState

data class PokemonListState(
    val list: List<PokemonDetailsState> = emptyList()
)

