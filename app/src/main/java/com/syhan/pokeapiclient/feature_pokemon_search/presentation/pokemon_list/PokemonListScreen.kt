package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.common.data.NavDestinations
import com.syhan.pokeapiclient.common.domain.NetworkResponse
import com.syhan.pokeapiclient.common.presentation.LoadingScreen
import com.syhan.pokeapiclient.common.presentation.NetworkErrorScreen
import com.syhan.pokeapiclient.feature_pokemon_search.data.PokemonSorting
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
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    when (networkState) {
        NetworkResponse.InitialLoading -> {
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
                loadRandomizedList = viewModel::loadRandomizedList,
                loadMoreItems = viewModel::loadMoreItems,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    items: List<PokemonShortDetailsState>,
    onCardClick: (id: Int) -> Unit,
    loadRandomizedList: () -> Unit,
    loadMoreItems: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { PokemonListAppBar(scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(R.string.randomize_list),
                        fontSize = 18.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_dice),
                        contentDescription = null
                    )
                },
                onClick = loadRandomizedList
            )
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
    var selectedStat by rememberSaveable { mutableStateOf<PokemonSorting>(PokemonSorting.SortByNumber) }

    LaunchedEffect(reachedLoadingPoint) {
        if (reachedLoadingPoint) {
            loadMoreItems()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SortingMenuBox(
                    isMenuExpanded = isMenuExpanded,
                    statName = selectedStat.statName,
                    statImage = selectedStat.statImage,
                    onClick = {
                        isMenuExpanded = !isMenuExpanded
                    },
                ) {
                    SortingMenu(
                        isExpanded = isMenuExpanded,
                        onDismiss = { isMenuExpanded = false },
                        onStatClick = {
                            isMenuExpanded = false
                            selectedStat = it
                        }
                    )
                }
            }
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

@Composable
private fun SortingMenu(
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    onStatClick: (PokemonSorting) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        offset = DpOffset(x = 0.dp, y = 14.dp)
    ) {
        val sortingList = PokemonSorting.entries
        sortingList.forEach { stat ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(stat.statImage),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(stat.statName),
                        fontSize = 18.sp
                    )
                },
                onClick = { onStatClick(stat) }
            )
        }
    }
}

@Composable
private fun SortingMenuBox(
    isMenuExpanded: Boolean,
    @StringRes statName: Int,
    @DrawableRes statImage: Int,
    onClick: () -> Unit,
    menu: @Composable (() -> Unit)
) {
    val colors = MaterialTheme.colorScheme
    /* this isn't confusing at all */
    OutlinedTextField(
        colors = if (isMenuExpanded) {
            OutlinedTextFieldDefaults.colors(
                disabledContainerColor = colors.surfaceContainer,
                disabledTrailingIconColor = colors.secondary,
                disabledBorderColor = colors.onSurface,
                disabledLeadingIconColor = colors.onSurface
            )
        } else {
            OutlinedTextFieldDefaults.colors(
                disabledContainerColor = colors.surfaceContainerLow,
                disabledTrailingIconColor = colors.secondary,
                disabledBorderColor = colors.secondaryContainer,
                disabledLeadingIconColor = colors.secondary
            )
        },
        prefix = {
            Row {
                Text(
                    text = stringResource(R.string.sort_by) + " ",
                    fontSize = 18.sp,
                    color = colors.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(statName),
                    fontSize = 18.sp,
                    color = colors.onSurface
                )
            }
        },
        value = "",
        onValueChange = {},
        textStyle = TextStyle(
            color = colors.onSurface,
            fontSize = 18.sp
        ),
        enabled = false,
        leadingIcon = {
            Icon(
                painter = painterResource(statImage),
                contentDescription = null,
            )
        },
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            if (isMenuExpanded) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.action_collapse)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.action_expand)
                )
            }
            menu()
        },
        modifier = Modifier
            .clickable(
                interactionSource = null,
                indication = null
            ) { onClick() }
            .fillMaxWidth()
    )
}