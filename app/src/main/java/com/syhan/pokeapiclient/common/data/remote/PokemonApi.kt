package com.syhan.pokeapiclient.common.data.remote

import com.syhan.pokeapiclient.common.domain.model.PokemonFullInfo
import com.syhan.pokeapiclient.common.domain.model.PokemonResultList
import com.syhan.pokeapiclient.common.domain.model.PokemonShortInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("/api/v2/pokemon/{id}/")
    suspend fun getShortPokemonById(
        @Path("id") id: Int,
    ) : Response<PokemonShortInfo>

    @GET("/api/v2/pokemon/{id}/")
    suspend fun getFullPokemonById(
        @Path("id") id: Int,
    ) : Response<PokemonFullInfo>

    @GET("/api/v2/pokemon/")
    suspend fun getMultiplePokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ) : Response<PokemonResultList>
}
