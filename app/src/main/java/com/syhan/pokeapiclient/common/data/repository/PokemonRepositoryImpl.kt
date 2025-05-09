package com.syhan.pokeapiclient.common.data.repository

import com.syhan.pokeapiclient.common.data.remote.PokemonApi
import com.syhan.pokeapiclient.common.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val api: PokemonApi
) : PokemonRepository {
    override suspend fun getShortPokemonById(id: Int) = api.getShortPokemonById(id)

    override suspend fun getFullPokemonById(id: Int) = api.getFullPokemonById(id)

    override suspend fun getMultiplePokemon(limit: Int, offset: Int) =
        api.getMultiplePokemon(limit, offset)
}