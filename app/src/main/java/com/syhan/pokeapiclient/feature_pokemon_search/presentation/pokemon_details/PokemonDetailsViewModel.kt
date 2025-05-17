package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.domain.NetworkRequestState
import com.syhan.pokeapiclient.common.domain.NetworkRequestStateHandler.setError
import com.syhan.pokeapiclient.common.domain.NetworkRequestStateHandler.setLoading
import com.syhan.pokeapiclient.common.domain.NetworkRequestStateHandler.setSuccess
import com.syhan.pokeapiclient.common.domain.util.capitalizeFirstChar
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "PokemonDetailsViewModel"

class PokemonDetailsViewModel(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailsState = MutableStateFlow(PokemonDetailsState())
    val detailsState = _detailsState.asStateFlow()

    private val _networkState =
        MutableStateFlow<NetworkRequestState>(NetworkRequestState.Loading)
    val networkState = _networkState.asStateFlow()

    private val currentPokemonId = savedStateHandle.get<Int>("currentPokemonId")

    init {
        tryLoadingData()
    }

    fun tryLoadingData() {
        currentPokemonId?.let { id ->
            if (id != -1) {
                getFullPokemonInfo(id)
            }
        }
    }

    private fun getFullPokemonInfo(id: Int) {
        viewModelScope.launch {
            _networkState.setLoading()
            try {
                val response = repository.getPokemonById(id)
                response.body()?.let {
                    _detailsState.value = detailsState.value.copy(
                        id = it.id,
                        name = it.name.capitalizeFirstChar(),
                        sprites = it.sprites,
                        height = it.height,
                        weight = it.weight,
                        types = it.types.map { type ->
                            type.copy(
                                type = type.type.copy(
                                    name = type.type.name.capitalizeFirstChar()
                                )
                            )
                        },
                        stats = it.stats.map { stat ->
                            stat.copy(
                                stat = stat.stat.copy(
                                    name = stat.stat.name.capitalizeFirstChar()
                                )
                            )
                        },
                    )
                    _networkState.setSuccess()
                }
            } catch (e: Exception) {
                _networkState.setError(e)
            }
        }
    }
}