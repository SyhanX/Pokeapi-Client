package com.syhan.pokeapiclient.common.domain.repository

import com.syhan.pokeapiclient.common.domain.model.Pokemon
import com.syhan.pokeapiclient.common.domain.model.PokemonResultList
import retrofit2.Response

interface PokemonRepository {
    suspend fun getPokemonById(id: Int) : Response<Pokemon>
    suspend fun getMultiplePokemon(limit: Int, offset: Int) : Response<PokemonResultList>
}