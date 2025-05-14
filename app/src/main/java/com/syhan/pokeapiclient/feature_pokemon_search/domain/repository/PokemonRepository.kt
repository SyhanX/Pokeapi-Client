package com.syhan.pokeapiclient.feature_pokemon_search.domain.repository

import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonDetails
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonResultList
import retrofit2.Response

interface PokemonRepository {
    suspend fun getPokemonById(id: Int) : Response<PokemonDetails>
    suspend fun getPokemonList(limit: Int, offset: Int) : Response<PokemonResultList>
}