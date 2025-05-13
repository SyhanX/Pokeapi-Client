package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.domain.NetworkResponse
import com.syhan.pokeapiclient.common.domain.setHttpException
import com.syhan.pokeapiclient.common.domain.setInitialLoading
import com.syhan.pokeapiclient.common.domain.setIoException
import com.syhan.pokeapiclient.common.domain.setSuccess
import com.syhan.pokeapiclient.common.domain.setUnknownException
import com.syhan.pokeapiclient.common.domain.util.capitalizeFirstChar
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details.PokemonShortDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import kotlin.random.Random

private const val TAG = "PokemonListViewModel"

class PokemonListViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _listState = MutableStateFlow(PokemonListState())
    val listState = _listState.asStateFlow()

    private val _networkState = MutableStateFlow<NetworkResponse>(NetworkResponse.InitialLoading)
    val networkState = _networkState.asStateFlow()

    private val detailsList = mutableSetOf<PokemonShortDetailsState>()

    init {
        tryLoadingPokemonList()
    }

    private fun addOffsetToList() {
        _listState.value = listState.value.copy(
            offset = listState.value.offset + listState.value.itemsPerPage
        )
    }

    fun tryLoadingPokemonList() {
        _networkState.setInitialLoading()
        loadDetailedPokemonList()
        addOffsetToList()
    }

    fun loadMoreItems() {
        loadDetailedPokemonList()
        addOffsetToList()
    }

    fun loadRandomizedList() {
        val minPokemonId = 0
        val maxPokemonId = 1302
        /* subtract the max range so that the list doesn't loop during the first load */
        val randomizedOffset = Random
            .nextInt(minPokemonId, maxPokemonId - listState.value.itemsPerPage)

        _listState.value = listState.value.copy(
            offset = randomizedOffset
        )
        _networkState.setInitialLoading()

        detailsList.clear()

        loadDetailedPokemonList()
        addOffsetToList()
    }

    private fun loadDetailedPokemonList() {
        viewModelScope.launch {
            try {
                val resultList = repository.getMultiplePokemon(
                    limit = listState.value.itemsPerPage,
                    offset = listState.value.offset
                ).body() ?: throw IOException()

                /* transforming url list into id list */
                val pokemonIdList: List<Int> = resultList.results.map {
                    it.url
                        .removePrefix("https://pokeapi.co/api/v2/pokemon/")
                        .removeSuffix("/")
                        .toInt()
                }

                pokemonIdList.forEach { id ->
                    repository
                        .getShortPokemonById(id)
                        .body()
                        ?.let { details ->
                            detailsList.add(
                                PokemonShortDetailsState(
                                    id = details.id,
                                    name = details.name.capitalizeFirstChar(),
                                    sprites = details.sprites,
                                    types = details.types
                                )
                            )
                        }
                }

                _listState.value = listState.value.copy(
                    /* converting this list to a new list triggers a recomposition in PokemonListScreen
                    * it took me several hours to figure this out and it's 2 hours past midnight now
                    * my mental anguish is immeasurable */
                    pokemonDetailsList = detailsList.toList(),
                )
                _networkState.setSuccess()
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