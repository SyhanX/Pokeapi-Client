package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.syhan.pokeapiclient.common.data.NavDestinations
import com.syhan.pokeapiclient.common.presentation.ConnectionHandlingScreen
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.PokemonCard
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details.PokemonShortDetailsState
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PokemonListScreen"

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = koinViewModel(),
    navController: NavController,
) {
    val state = viewModel.listState.collectAsStateWithLifecycle()
    val networkState = viewModel.networkState.collectAsStateWithLifecycle()

    ConnectionHandlingScreen(
        response = networkState.value,
        onRetry = viewModel::loadData
    ) {
        PokemonListContent(
            state = state.value,
            onCardClick = { id ->
                navController.navigate(
                    NavDestinations.PokemonDetailsScreen(id)
                )
            }
        )
    }
}

@Composable
fun PokemonListContent(
    state: PokemonListState,
    onCardClick: (id: Int) -> Unit,
) {
    Scaffold { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .fillMaxSize()
        ) {
            items(
                items = state.list,
                key = { pokemon: PokemonShortDetailsState ->
                    pokemon.name
                }
            ) {
                PokemonCard(
                    name = it.name,
                    sprite = it.sprites.frontDefault ?: "",
                    onClick = {
                        Log.d(TAG, "PokemonListContent: clicked on ${it.id}")
                        onCardClick(it.id)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PokemonListPreview() {
    PokeapiClientTheme {

    }
}