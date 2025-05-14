package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list.state

import com.syhan.pokeapiclient.feature_pokemon_search.data.ListSortingType

data class PokemonListState(
    val pokemonDetailsList: List<PokemonCardState> = emptyList(),
    val offset: Int = 0,
    val itemsPerPage: Int = 30,
    val isRandomizingEnabled: Boolean = true,
    val isSortingEnabled: Boolean = false,
    val sortingType: ListSortingType = ListSortingType.SortByNumber,
    val isSortOrderAscending: Boolean = true
)