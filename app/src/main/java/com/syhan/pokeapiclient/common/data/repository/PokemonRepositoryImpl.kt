package com.syhan.pokeapiclient.common.data.repository

import com.syhan.pokeapiclient.common.data.remote.PokemonApi
import com.syhan.pokeapiclient.common.domain.model.Pokemon
import com.syhan.pokeapiclient.common.domain.model.PokemonResultList
import com.syhan.pokeapiclient.common.domain.repository.PokemonRepository
import retrofit2.Response

class PokemonRepositoryImpl(
    private val api: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemonById(id: Int): Response<Pokemon> {
        return api.getPokemonById(id)
    }

    override suspend fun getMultiplePokemon(limit: Int, offset: Int): Response<PokemonResultList> {
        return api.getMultiplePokemon(limit, offset)
    }
}