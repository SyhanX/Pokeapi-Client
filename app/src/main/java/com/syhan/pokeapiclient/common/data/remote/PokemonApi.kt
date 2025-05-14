package com.syhan.pokeapiclient.common.data.remote

import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonDetails
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonResultList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("/api/v2/pokemon/{id}/")
    suspend fun getPokemonById(
        @Path("id") id: Int,
    ) : Response<PokemonDetails>

    @GET("/api/v2/pokemon/")
    suspend fun getMultiplePokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ) : Response<PokemonResultList>
}
