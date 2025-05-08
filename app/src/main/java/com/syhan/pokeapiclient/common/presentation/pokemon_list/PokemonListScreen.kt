package com.syhan.pokeapiclient.common.presentation.pokemon_list

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
import com.syhan.pokeapiclient.common.presentation.components.PokemonCard
import com.syhan.pokeapiclient.common.presentation.pokemon_details.PokemonShortDetailsState
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PokemonListScreen"

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = koinViewModel(),
    navController: NavController,
) {
    val state = viewModel.listState.collectAsStateWithLifecycle()
    val networkState = viewModel.networkState.collectAsStateWithLifecycle()

    Log.d(TAG, "PokemonListScreen: $networkState")
    PokemonListContent(
        state = state.value
    )
}

@Composable
fun PokemonListContent(
    state: PokemonListState,
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
                    sprite = it.sprites.frontDefault ?: ""
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