package com.syhan.pokeapiclient.common.presentation.pokemon_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.data.NetworkErrorType
import com.syhan.pokeapiclient.common.data.NetworkResponse
import com.syhan.pokeapiclient.common.domain.model.PokemonResultList
import com.syhan.pokeapiclient.common.domain.repository.PokemonRepository
import com.syhan.pokeapiclient.common.presentation.pokemon_details.PokemonShortDetailsState
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

    private val basePokemonUrl = "https://pokeapi.co/api/v2/pokemon/"

    private val detailsList = mutableListOf<PokemonShortDetailsState>()

    init {
        viewModelScope.launch {
            val list = getPokemonList()
            list?.let {
                getDetailedPokemonInfo(it)
                _listState.value = listState.value.copy(
                    list = detailsList
                )
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
            _networkState.value = NetworkResponse.Error(NetworkErrorType.UnexpectedError)
            return@withContext null
        }
    }

    private suspend fun getDetailedPokemonInfo(list: PokemonResultList) {
        _networkState.value = NetworkResponse.Loading
        Log.d(TAG, "getDetailedPokemonInfo: trying to load")
        try {
            val pokemonIdList = list.results.map { result ->
                result.url.removePrefix(basePokemonUrl).removeSuffix("/")
            }
            Log.d(TAG, "ids: $pokemonIdList")
            pokemonIdList.forEach { id ->
                val response = repository.getPokemonById(id.toInt())
                val body = response.body()

                body?.let { pokemon ->
                    val capitalizedName = pokemon.name.replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.uppercase()
                        } else it.toString()
                    }
                    detailsList.add(
                        PokemonShortDetailsState(
                            name = capitalizedName,
                            sprites = pokemon.sprites
                        )
                    )
                }
            }
            Log.d(TAG, "detailedlist: $detailsList")
            _networkState.value = NetworkResponse.Success
            Log.d(TAG, "getDetailedPokemonInfo: loaded successfully")
        } catch (e: IOException) {
            _networkState.value = NetworkResponse.Error(NetworkErrorType.NoInternetError)
        } catch (e: Exception) {
            _networkState.value = NetworkResponse.Error(NetworkErrorType.UnexpectedError)
        }

    }
}