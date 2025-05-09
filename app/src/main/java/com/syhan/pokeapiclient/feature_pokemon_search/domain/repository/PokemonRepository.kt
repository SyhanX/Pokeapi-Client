package com.syhan.pokeapiclient.feature_pokemon_search.domain.repository

import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonFullInfo
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonResultList
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonShortInfo
import retrofit2.Response

interface PokemonRepository {
    suspend fun getShortPokemonById(id: Int) : Response<PokemonShortInfo>
    suspend fun getFullPokemonById(id: Int) : Response<PokemonFullInfo>
    suspend fun getMultiplePokemon(limit: Int, offset: Int) : Response<PokemonResultList>
}