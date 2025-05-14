package com.syhan.pokeapiclient.feature_pokemon_search.data.repository

import com.syhan.pokeapiclient.common.data.remote.PokemonApi
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val api: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemonById(id: Int) = api.getPokemonById(id)

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ) = api.getMultiplePokemon(limit, offset)
}