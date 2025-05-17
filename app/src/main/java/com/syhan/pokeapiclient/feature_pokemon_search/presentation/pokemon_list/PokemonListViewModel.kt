package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhan.pokeapiclient.common.domain.NetworkRequestState
import com.syhan.pokeapiclient.common.domain.NetworkRequestStateHandler.setError
import com.syhan.pokeapiclient.common.domain.NetworkRequestStateHandler.setLoading
import com.syhan.pokeapiclient.common.domain.NetworkRequestStateHandler.setSuccess
import com.syhan.pokeapiclient.common.domain.util.capitalizeFirstChar
import com.syhan.pokeapiclient.feature_pokemon_search.data.ListSortingType
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.ATTACK
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.DEFENSE
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.HP
import com.syhan.pokeapiclient.feature_pokemon_search.domain.repository.PokemonRepository
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list.state.PokemonCardState
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list.state.PokemonListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import kotlin.random.Random

private const val TAG = "PokemonListViewModel"

class PokemonListViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _listState = MutableStateFlow(PokemonListState())
    val listState = _listState.asStateFlow()

    private val _networkState = MutableStateFlow<NetworkRequestState>(NetworkRequestState.Loading)
    val networkState = _networkState.asStateFlow()

    private val detailsList = mutableSetOf<PokemonCardState>()

    init {
        tryLoadingInitialItems()
    }

    private fun addToOffsetValue() {
        _listState.value = listState.value.copy(
            offset = listState.value.offset + listState.value.itemsPerPage
        )
    }

    private fun resetOffset() {
        _listState.value = listState.value.copy(
            offset = 0
        )
    }

    fun tryLoadingInitialItems() {
        _networkState.setLoading()
        resetOffset()
        detailsList.clear()
        loadDetailedPokemonList()
        addToOffsetValue()
    }

    fun tryLoadingMoreItems() {
        loadDetailedPokemonList()
        addToOffsetValue()
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
        _networkState.setLoading()

        detailsList.clear()

        loadDetailedPokemonList()
        addToOffsetValue()
    }

    private fun loadDetailedPokemonList() {
        viewModelScope.launch {
            _listState.value = listState.value.copy(
                isRandomizingEnabled = false
            )
            try {
                val resultList = repository.getPokemonList(
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
                        .getPokemonById(id)
                        .body()
                        ?.let { details ->
                            detailsList.add(
                                PokemonCardState(
                                    id = details.id,
                                    name = details.name.capitalizeFirstChar(),
                                    sprites = details.sprites,
                                    hpValue = details.stats[HP].baseStat,
                                    attackValue = details.stats[ATTACK].baseStat,
                                    defenseValue = details.stats[DEFENSE].baseStat,
                                    types = details.types.map {
                                        it.copy(
                                            type = it.type.copy(
                                                name = it.type.name.capitalizeFirstChar()
                                            )
                                        )
                                    }
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
            } catch (e: Exception) {
                _networkState.setError(e)
            } finally {
                _listState.value = listState.value.copy(
                    isRandomizingEnabled = true
                )
            }
        }
    }

    fun switchSortingMode(isEnabled: Boolean) {
        _listState.value = listState.value.copy(
            isSortingEnabled = !isEnabled,
        )
        sortListByStat(ListSortingType.SortByNumber, true)
    }

    fun sortListByStat(type: ListSortingType, isAscending: Boolean) {
        _listState.value = listState.value.copy(
            sortingType = type,
            isSortOrderAscending = isAscending
        )
        val sortedList = detailsList.sortedBy {
            when (type) {
                ListSortingType.SortByNumber -> it.id
                ListSortingType.SortByHp -> it.hpValue
                ListSortingType.SortByAttack -> it.attackValue
                ListSortingType.SortByDefense -> it.defenseValue
            }
        }.let {
            if (isAscending) it else it.reversed()
        }
        _listState.value = listState.value.copy(
            pokemonDetailsList = sortedList
        )
    }
}