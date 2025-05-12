package com.syhan.pokeapiclient.feature_pokemon_search.domain.repository

import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonDetailsFull
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonDetailsShort
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonResultList
import retrofit2.Response

interface PokemonRepository {
    suspend fun getShortPokemonById(id: Int) : Response<PokemonDetailsShort>
    suspend fun getFullPokemonById(id: Int) : Response<PokemonDetailsFull>
    suspend fun getMultiplePokemon(limit: Int, offset: Int) : Response<PokemonResultList>
}