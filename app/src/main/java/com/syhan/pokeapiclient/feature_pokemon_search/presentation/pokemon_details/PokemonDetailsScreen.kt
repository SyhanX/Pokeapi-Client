package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonDetailsViewModel = koinViewModel()
) {
    val state = viewModel.detailsState.collectAsStateWithLifecycle()
    val networkState = viewModel.networkState.collectAsStateWithLifecycle()

    PokemonDetailsContent(
        state = state.value
    )
}

@Composable
fun PokemonDetailsContent(
    state: PokemonFullDetailsState
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Text(
                text = state.toString()
            )
        }
    }
}

@Preview
@Composable
private fun PokemonDetailsPreview() {
    PokeapiClientTheme {
/*        PokemonDetailsContent()*/
    }
}
