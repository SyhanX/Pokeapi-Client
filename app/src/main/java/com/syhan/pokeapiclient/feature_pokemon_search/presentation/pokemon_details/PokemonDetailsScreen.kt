package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.common.domain.NetworkRequestState
import com.syhan.pokeapiclient.common.presentation.LoadingScreen
import com.syhan.pokeapiclient.common.presentation.NetworkErrorScreen
import com.syhan.pokeapiclient.feature_pokemon_search.data.util.addLeadingZeros
import com.syhan.pokeapiclient.feature_pokemon_search.data.util.findTypeColor
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.ATTACK
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.DEFENSE
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.HP
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.SPECIAL_ATTACK
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.SPECIAL_DEFENSE
import com.syhan.pokeapiclient.feature_pokemon_search.domain.PokemonStatIndex.SPEED
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Sprites
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Stat
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.PokemonTypeTag
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PokemonDetailsScreen"

@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonDetailsViewModel = koinViewModel(),
    navController: NavHostController
) {
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()
    val detailsState by viewModel.detailsState.collectAsStateWithLifecycle()

    when (networkState) {
        NetworkRequestState.Loading -> {
            LoadingScreen()
        }

        is NetworkRequestState.Error -> {
            NetworkErrorScreen(
                errorType = (networkState as NetworkRequestState.Error).type,
                onRetry = viewModel::tryLoadingData
            )
        }

        NetworkRequestState.Success -> {
            PokemonDetailsContent(
                state = detailsState,
                navigateUp = navController::navigateUp
            )
        }
    }
}

@Composable
fun PokemonDetailsContent(
    state: PokemonDetailsState,
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            PokemonDetailsAppBar(
                navigateUp = navigateUp,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .fillMaxSize()
        ) {
            item {
              PokemonImageAndName(
                  name = state.name,
                  types = state.types,
                  sprites = state.sprites
              )
            }
            item {
              BasicInfoCard(
                  id = state.id,
                  height = state.height,
                  weight = state.weight
              )
            }
            item {
                TypesCard(state.types)
            }
            item {
                StatsCard(state.stats)
            }
        }
    }
}

@Composable
private fun PokemonImageAndName(
    name: String,
    types: List<Type>,
    sprites: Sprites
) {
    val typeColors = mutableListOf<Color>()
    types.forEach {
        typeColors.add(findTypeColor(it.type.name))
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = sprites.frontDefault,
            contentDescription = name,
            modifier = Modifier
                .size(200.dp)
                .border(
                    brush = Brush.linearGradient(
                        colors = if (typeColors.size <= 1) {
                            listOf(typeColors[0], typeColors[0])
                        } else typeColors
                    ),
                    width = 4.dp,
                    shape = RoundedCornerShape(20)
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = if (typeColors.size <= 1) {
                            listOf(typeColors[0], typeColors[0])
                        } else typeColors
                    ),
                    shape = RoundedCornerShape(20),
                    alpha = 0.25f
                )
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BasicInfoCard(
    id: Int,
    height: Int,
    weight: Int
) {
    Card(
        shape = RoundedCornerShape(15)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = stringResource(R.string.national_number),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "#${addLeadingZeros(id)}",
                    fontSize = 18.sp
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.height),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${height / 10f} m",
                    fontSize = 18.sp
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.weight),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${weight / 10f} kg",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun TypesCard(types: List<Type>) {
    Card(
        shape = RoundedCornerShape(15)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(
                    if (types.size == 1) R.string.type_singular
                    else R.string.types_plural
                ),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                types.forEach { type ->
                    PokemonTypeTag(
                        name = type.type.name,
                        color = findTypeColor(type.type.name),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsCard(stats: List<Stat>) {
    Card(
        shape = RoundedCornerShape(15)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.stats),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stats[HP].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[HP].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = stringResource(R.string.special_attack),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[SPECIAL_ATTACK].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stats[ATTACK].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[ATTACK].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = stringResource(R.string.special_defense),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[SPECIAL_DEFENSE].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stats[DEFENSE].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[DEFENSE].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = stats[SPEED].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[SPEED].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}