package com.syhan.pokeapiclient.common.domain.repository

import com.syhan.pokeapiclient.common.domain.model.PokemonShortInfo
import com.syhan.pokeapiclient.common.domain.model.PokemonResultList
import retrofit2.Response

interface PokemonRepository {
    suspend fun getPokemonById(id: Int) : Response<PokemonShortInfo>
    suspend fun getMultiplePokemon(limit: Int, offset: Int) : Response<PokemonResultList>
}