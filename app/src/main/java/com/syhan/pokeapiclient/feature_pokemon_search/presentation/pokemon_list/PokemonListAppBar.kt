package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListAppBar(scrollBehavior: TopAppBarScrollBehavior) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.pokemon_search),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        scrollBehavior = scrollBehavior
    )
}