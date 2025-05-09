package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.data.NetworkErrorType
import com.syhan.pokeapiclient.common.data.NetworkResponse
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.PokemonResultList
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import com.syhan.pokeapiclient.common.domain.util.doSimpleNetworkRequest
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details.PokemonShortDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

private const val TAG = "PokemonListViewModel"

class PokemonListViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _listState = MutableStateFlow(PokemonListState())
    val listState = _listState.asStateFlow()

    private val _networkState = MutableStateFlow<NetworkResponse>(NetworkResponse.Loading)
    val networkState = _networkState.asStateFlow()

    private val detailsList = mutableListOf<PokemonShortDetailsState>()

    init {
        viewModelScope.launch {
            val list = getPokemonList()
            list?.let { result ->
                loadShortPokemonInfo(result)
            }
        }
    }

    private suspend fun getPokemonList(): PokemonResultList? = withContext(Dispatchers.IO) {
        _networkState.value = NetworkResponse.Loading

        try {
            val response = repository.getMultiplePokemon(5, 0)
            val body = response.body()
            _networkState.value = NetworkResponse.Success
            return@withContext body
        } catch (e: IOException) {
            _networkState.value = NetworkResponse.Error(NetworkErrorType.NoInternetError)
            return@withContext null
        } catch (e: HttpException) {
            _networkState.value = NetworkResponse.Error(NetworkErrorType.UnexpectedNetworkError)
            return@withContext null
        }
    }

    private fun loadShortPokemonInfo(list: PokemonResultList) {
        doSimpleNetworkRequest(_networkState, viewModelScope) {
            val pokemonIdList = list.results.map { result ->
                /*trimming the url so that it only returns the id of a pokemon*/
                result.url
                    .removePrefix("https://pokeapi.co/api/v2/pokemon/")
                    .removeSuffix("/")
                    .toInt()
            }
            pokemonIdList.forEach { id ->
                val response = repository.getShortPokemonById(id)
                val body = response.body()

                body?.let { pokemon ->
                    val capitalizedName = pokemon.name.replaceFirstChar {
                        if (it.isLowerCase()) it.uppercase()
                        else it.toString()
                    }

                    detailsList.add(
                        PokemonShortDetailsState(
                            id = pokemon.id,
                            name = capitalizedName,
                            sprites = pokemon.sprites
                        )
                    )
                }
            }
            _listState.value = listState.value.copy(
                list = detailsList
            )
        }
    }
}