package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.common.data.NavDestinations
import com.syhan.pokeapiclient.common.domain.NetworkResponse
import com.syhan.pokeapiclient.common.presentation.LoadingScreen
import com.syhan.pokeapiclient.common.presentation.NetworkErrorScreen
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.PokemonCard
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details.PokemonShortDetailsState
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PokemonListScreen"

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = koinViewModel(),
    navController: NavController,
) {
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val listState by viewModel.listState.collectAsState()
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
                onCardClick = { id ->
                    navController.navigate(
                        NavDestinations.PokemonDetailsScreen(id)
                    )
                },
                onFabClick = {
                    /*TODO*/
                },
                loadMoreItems = {
                    viewModel.loadMoreItems()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    items: List<PokemonShortDetailsState>,
    onCardClick: (id: Int) -> Unit,
    onFabClick: () -> Unit,
    loadMoreItems: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { PokemonListAppBar(scrollBehavior) },
        floatingActionButton = {
            FloatingActionButton(onClick = loadMoreItems) {
                Icon(
                    painter = painterResource(R.drawable.ic_dice),
                    contentDescription = null
                )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PokemonList(
            modifier = Modifier.padding(innerPadding),
            items = items,
            onCardClick = { onCardClick(it) },
            loadMoreItems = loadMoreItems,
        )
    }
}

@Composable
private fun PokemonList(
    modifier: Modifier = Modifier,
    items: List<PokemonShortDetailsState>,
    onCardClick: (id: Int) -> Unit,
    loadMoreItems: () -> Unit,
) {
    val lazyColumnState = rememberLazyListState()
    val buffer = 10
    /* the point in the LazyColumn at which the loadMoreItems() will be triggered*/
    val reachedLoadingPoint: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyColumnState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = lazyColumnState.layoutInfo.totalItemsCount
            (lastVisibleItem?.index != 0) && (lastVisibleItem?.index == (totalItemsCount - buffer))
        }
    }

    LaunchedEffect(reachedLoadingPoint) {
        if (reachedLoadingPoint) loadMoreItems()
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
            Spacer(Modifier)
        }
        items(
            items = items,
            key = { pokemon: PokemonShortDetailsState -> pokemon.id }
        ) { state ->
            PokemonCard(
                id = state.id,
                name = state.name,
                sprite = state.sprites.frontDefault.toString(),
                types = state.types,
                onClick = { onCardClick(state.id) },
                modifier = Modifier.animateItem()
            )
        }
        item {
            if (items.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}