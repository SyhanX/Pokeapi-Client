package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.domain.NetworkResponse
import com.syhan.pokeapiclient.common.domain.setHttpException
import com.syhan.pokeapiclient.common.domain.setIoException
import com.syhan.pokeapiclient.common.domain.setLoading
import com.syhan.pokeapiclient.common.domain.setSuccess
import com.syhan.pokeapiclient.common.domain.setUnknownException
import com.syhan.pokeapiclient.common.domain.util.capitalizeFirstChar
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

private const val TAG = "PokemonDetailsViewModel"

class PokemonDetailsViewModel(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailsState = MutableStateFlow(PokemonDetailsState())
    val detailsState = _detailsState.asStateFlow()

    private val _networkState =
        MutableStateFlow<NetworkResponse>(NetworkResponse.Loading)
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
                        stats = it.stats.map { stat ->
                            stat.copy(
                                stat = stat.stat.copy(
                                    name = stat.stat.name.capitalizeFirstChar()
                                )
                            )
                        },
                        types = it.types.map { type ->
                            type.copy(
                                type = type.type.copy(
                                    name = type.type.name.capitalizeFirstChar()
                                )
                            )
                        },
                        height = it.height,
                        weight = it.weight
                    )
                    _networkState.setSuccess()
                }
            } catch (e: IOException) {
                _networkState.setIoException(e)
            } catch (e: HttpException) {
                _networkState.setHttpException(e)
            } catch (e: Exception) {
                _networkState.setUnknownException(e)
            }
        }
    }
}