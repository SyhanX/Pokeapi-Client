package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.syhan.pokeapiclient.common.data.NavDestinations
import com.syhan.pokeapiclient.common.domain.NetworkResponse
import com.syhan.pokeapiclient.common.presentation.LoadingScreen
import com.syhan.pokeapiclient.common.presentation.NetworkErrorScreen
import com.syhan.pokeapiclient.feature_pokemon_search.data.PokemonSortingType
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.PokemonCard
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.RandomizeListAnimatedFAB
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.ScrollUpAnimatedFAB
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.SortingMenu
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.SortingMenuBox
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list.state.PokemonCardState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PokemonListScreen"

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = koinViewModel(),
    navController: NavController,
) {
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    when (networkState) {
        NetworkResponse.Loading -> {
            LoadingScreen()
        }

        is NetworkResponse.Error -> {
            NetworkErrorScreen(
                errorType = (networkState as NetworkResponse.Error).type,
                onRetry = viewModel::tryLoadingPokemonList
            )
        }

        NetworkResponse.Success -> {
            PokemonListContent(
                items = listState.pokemonDetailsList,
                sortingType = listState.sortingType,
                isSortingEnabled = listState.isSortingEnabled,
                isSortingAscending = listState.sortOrderAscending,
                isRandomizingEnabled = listState.isRandomizingEnabled,
                loadMoreItems = viewModel::loadMoreItems,
                loadRandomizedList = viewModel::loadRandomizedList,
                onCheckedChange = viewModel::switchSortingMode,
                onCardClick = { id ->
                    navController.navigate(
                        NavDestinations.PokemonDetailsScreen(id)
                    )
                },
                onStatSelect = {
                    viewModel.sortListByStat(
                        type = it,
                        isAscending = listState.sortOrderAscending
                    )
                },
                onSortingOrderSelect = {
                    viewModel.sortListByStat(
                        type = listState.sortingType,
                        isAscending = it
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    items: List<PokemonCardState>,
    isSortingEnabled: Boolean,
    sortingType: PokemonSortingType,
    onCardClick: (id: Int) -> Unit,
    loadRandomizedList: () -> Unit,
    loadMoreItems: () -> Unit,
    onStatSelect: (PokemonSortingType) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    isSortingAscending: Boolean,
    isRandomizingEnabled: Boolean,
    onSortingOrderSelect: (Boolean) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val lazyColumnState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { PokemonListAppBar(scrollBehavior) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ScrollUpAnimatedFAB(
                    isVisible = lazyColumnState.canScrollBackward,
                    onClick = {
                        scope.launch {
                            lazyColumnState.animateScrollToItem(0)
                        }
                    }
                )
                RandomizeListAnimatedFAB(
                    isVisible = !isSortingEnabled && isRandomizingEnabled,
                    onClick = loadRandomizedList
                )
            }
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) { innerPadding ->
        PokemonList(
            modifier = Modifier.padding(innerPadding),
            items = items,
            lazyColumnState = lazyColumnState,
            sortingType = sortingType,
            isSortingEnabled = isSortingEnabled,
            onCardClick = { onCardClick(it) },
            loadMoreItems = loadMoreItems,
            onStatSelect = { onStatSelect(it) },
            onCheckedChange = {
                onCheckedChange(!it)
            },
            onSortingOrderSelect = { onSortingOrderSelect(it) },
            isSortingAscending = isSortingAscending
        )
    }
}

@Composable
private fun PokemonList(
    modifier: Modifier = Modifier,
    items: List<PokemonCardState>,
    lazyColumnState: LazyListState,
    isSortingEnabled: Boolean,
    isSortingAscending: Boolean,
    sortingType: PokemonSortingType,
    loadMoreItems: () -> Unit,
    onCardClick: (id: Int) -> Unit,
    onStatSelect: (PokemonSortingType) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onSortingOrderSelect: (Boolean) -> Unit,
) {
    val buffer = 1
    /* the point in the LazyColumn at which the loadMoreItems() will be triggered*/
    val reachedLoadingPoint: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyColumnState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = lazyColumnState.layoutInfo.totalItemsCount
            (lastVisibleItem?.index != 0) && (lastVisibleItem?.index == (totalItemsCount - buffer))
        }
    }
    var isMenuExpanded by remember { mutableStateOf(false) }

    if (!isSortingEnabled) {
        LaunchedEffect(reachedLoadingPoint) {
            if (reachedLoadingPoint) {
                loadMoreItems()
            }
        }
    }

    LazyColumn(
        state = lazyColumnState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .animateContentSize()
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Enable sorting",
                        fontSize = 18.sp
                    )
                    Switch(
                        checked = isSortingEnabled,
                        onCheckedChange = { onCheckedChange(it) }
                    )
                }
                AnimatedVisibility(
                    visible = isSortingEnabled
                ) {
                    SortingMenuBox(
                        isMenuExpanded = isMenuExpanded,
                        statName = sortingType.selectedStatName,
                        statIcon = sortingType.selectedStatIcon,
                        onClick = {
                            isMenuExpanded = !isMenuExpanded
                        },
                        isSortingEnabled = isSortingEnabled
                    ) {
                        SortingMenu(
                            isExpanded = isMenuExpanded,
                            onDismiss = { isMenuExpanded = false },
                            onSortingAlgSelect = {
                                isMenuExpanded = false
                                onStatSelect(it)
                            },
                            isAscending = isSortingAscending,
                            onSortingOrderSelect = {
                                isMenuExpanded = false
                                onSortingOrderSelect(it)
                            }
                        )
                    }
                }
            }
        }
        item {
            HorizontalDivider()
        }
        items(
            items = items,
            key = { pokemon: PokemonCardState -> pokemon.id }
        ) { state ->
            PokemonCard(
                id = state.id,
                name = state.name,
                sprite = state.sprites.frontDefault.toString(),
                types = state.types,
                onClick = { onCardClick(state.id) },
                sortingType = sortingType,
                hpValue = state.hpValue,
                defenseValue = state.defenseValue,
                attackValue = state.attackValue,
                modifier = Modifier.animateItem()
            )
        }
        item {
            if (items.isEmpty()) return@item
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                if (!isSortingEnabled) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = "Disable sorting mode\nto load more items",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}