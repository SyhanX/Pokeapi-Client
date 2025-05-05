package com.syhan.pokeapiclient.common.data

import kotlinx.serialization.Serializable

sealed interface NavDestinations {

    @Serializable
    data object PokemonListScreen : NavDestinations

    @Serializable
    data object PokemonDetailsScreen : NavDestinations

}