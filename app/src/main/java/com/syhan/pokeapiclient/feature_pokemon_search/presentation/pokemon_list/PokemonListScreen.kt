package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.syhan.pokeapiclient.feature_pokemon_search.data.PokemonSortingAlgorithm
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
                sortingAlg = listState.sorting,
                isSortingEnabled = listState.isSortingEnabled,
                loadMoreItems = viewModel::loadMoreItems,
                loadRandomizedList = viewModel::loadRandomizedList,
                onCheckedChange = viewModel::switchSortingState,
                onCardClick = { id ->
                    navController.navigate(
                        NavDestinations.PokemonDetailsScreen(id)
                    )
                },
                onStatSelect = {

                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListContent(
    items: List<PokemonShortDetailsState>,
    isSortingEnabled: Boolean,
    sortingAlg: PokemonSortingAlgorithm,
    onCardClick: (id: Int) -> Unit,
    loadRandomizedList: () -> Unit,
    loadMoreItems: () -> Unit,
    onStatSelect: (PokemonSortingAlgorithm) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = { PokemonListAppBar(scrollBehavior) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = (!isSortingEnabled)
            ) {
                RandomizeListFAB(loadRandomizedList)
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PokemonList(
            modifier = Modifier.padding(innerPadding),
            items = items,
            sortingAlg = sortingAlg,
            isSortingEnabled = isSortingEnabled,
            onCardClick = { onCardClick(it) },
            loadMoreItems = loadMoreItems,
            onSelectStat = { onStatSelect(it) },
            onCheckedChange = {
                onCheckedChange(!it)
            }
        )
    }
}

@Composable
fun RandomizeListFAB(onClick: () -> Unit) {
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
        onClick = onClick
    )
}

@Composable
private fun PokemonList(
    modifier: Modifier = Modifier,
    items: List<PokemonShortDetailsState>,
    isSortingEnabled: Boolean,
    sortingAlg: PokemonSortingAlgorithm,
    loadMoreItems: () -> Unit,
    onCardClick: (id: Int) -> Unit,
    onSelectStat: (PokemonSortingAlgorithm) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
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
                        statName = sortingAlg.selectedStatName,
                        statIcon = sortingAlg.selectedStatIcon,
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
                                onSelectStat(it)
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
            if (items.isNotEmpty() && !isSortingEnabled) {
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
    onSortingAlgSelect: (PokemonSortingAlgorithm) -> Unit
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
        PokemonSortingAlgorithm.entries.forEach { algorithm ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(algorithm.selectedStatIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(algorithm.selectedStatName),
                        fontSize = 18.sp
                    )
                },
                onClick = { onSortingAlgSelect(algorithm) }
            )
        }
    }
}

@Composable
private fun SortingMenuBox(
    @StringRes statName: Int,
    @DrawableRes statIcon: Int,
    isMenuExpanded: Boolean,
    isSortingEnabled: Boolean,
    onClick: () -> Unit,
    menu: @Composable (() -> Unit)
) {
    val colors = MaterialTheme.colorScheme
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
                painter = painterResource(statIcon),
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
            .fillMaxWidth()
            .then(
                if (isSortingEnabled) {
                    Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
    )
}