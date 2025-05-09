package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.domain.NetworkResponse
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import com.syhan.pokeapiclient.common.domain.util.doSimpleNetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "PokemonDetailsViewModel"

class PokemonDetailsViewModel(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailsState = MutableStateFlow(PokemonFullDetailsState())
    val detailsState = _detailsState.asStateFlow()

    private val _networkState = MutableStateFlow<NetworkResponse>(NetworkResponse.Loading)
    val networkState = _networkState.asStateFlow()

    private val currentPokemonId = savedStateHandle.get<Int>("currentPokemonId")

    init {
        currentPokemonId?.let { id ->
            if (id != -1) {
                getFullPokemonInfo(id)
            }
        }
        Log.d(TAG, "init: $currentPokemonId")
    }

    private fun getFullPokemonInfo(id: Int) {
        doSimpleNetworkRequest(_networkState, viewModelScope) {
            val response = repository.getFullPokemonById(id)
            response.body()?.let {
                _detailsState.value = detailsState.value.copy(
                    id = it.id,
                    name = it.name,
                    sprites = it.sprites,
                    stats = it.stats,
                    types = it.types,
                    height = it.height,
                    weight = it.weight
                )
            }
        }
    }
}