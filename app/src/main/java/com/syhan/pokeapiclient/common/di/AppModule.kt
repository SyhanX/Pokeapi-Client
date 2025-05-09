package com.syhan.pokeapiclient.common.di

import androidx.lifecycle.SavedStateHandle
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.syhan.pokeapiclient.common.data.remote.PokemonApi
import com.syhan.pokeapiclient.feature_pokemon_search.data.repository.PokemonRepositoryImpl
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details.PokemonDetailsViewModel
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list.PokemonListViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retroJson = Json { ignoreUnknownKeys = true }

        Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .client(client)
            .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokemonApi::class.java)
    }

    single<PokemonRepository> {
        PokemonRepositoryImpl(get())
    }

    viewModel {
        PokemonListViewModel(get())
    }

    viewModel { (handle: SavedStateHandle) ->
        PokemonDetailsViewModel(get(), handle)
    }
}