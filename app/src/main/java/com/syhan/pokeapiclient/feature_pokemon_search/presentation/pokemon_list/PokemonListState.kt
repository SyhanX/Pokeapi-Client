package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details.PokemonShortDetailsState

data class PokemonListState(
    val list: List<PokemonShortDetailsState> = emptyList()
)

