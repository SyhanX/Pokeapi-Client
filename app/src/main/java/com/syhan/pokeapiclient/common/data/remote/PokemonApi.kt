package com.syhan.pokeapiclient.common.data.remote

import com.syhan.pokeapiclient.common.domain.model.Pokemon
import com.syhan.pokeapiclient.common.domain.model.PokemonResultList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("/api/v2/pokemon/{id}/")
    suspend fun getPokemonById(
        @Path("id") id: Int,
    ) : Response<Pokemon>

    @GET("/api/v2/pokemon/")
    suspend fun getMultiplePokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ) : Response<PokemonResultList>
}
