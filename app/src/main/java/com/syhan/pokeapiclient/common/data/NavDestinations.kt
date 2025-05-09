package com.syhan.pokeapiclient.common.data

import kotlinx.serialization.Serializable

sealed interface NavDestinations {

    @Serializable
    data object PokemonListScreen : NavDestinations

    @Serializable
    data class PokemonDetailsScreen(
        val currentPokemonId: Int,
    ) : NavDestinations

}