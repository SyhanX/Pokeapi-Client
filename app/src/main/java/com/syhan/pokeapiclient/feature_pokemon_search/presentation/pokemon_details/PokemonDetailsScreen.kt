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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.common.domain.util.capitalizeFirstLetter
import com.syhan.pokeapiclient.common.presentation.ConnectionHandlingScreen
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Stat
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type
import com.syhan.pokeapiclient.feature_pokemon_search.domain.util.findTypeColor
import com.syhan.pokeapiclient.feature_pokemon_search.presentation.components.PokemonTypeTag
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PokemonDetailsScreen"

@Composable
fun PokemonDetailsScreen(
    viewModel: PokemonDetailsViewModel = koinViewModel(),
    navController: NavHostController
) {
    val state = viewModel.detailsState.collectAsStateWithLifecycle()
    val networkState = viewModel.networkState.collectAsStateWithLifecycle()

    ConnectionHandlingScreen(
        response = networkState.value,
        onRetry = viewModel::loadData
    ) {
        PokemonDetailsContent(
            state = state.value,
            navigateUp = {
                navController.navigateUp()
            }
        )
    }
}

@Composable
fun PokemonDetailsContent(
    state: PokemonFullDetailsState,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .fillMaxSize()
        ) {
            item {
                val typeColors = mutableListOf<Color>()
                state.types.forEach {
                    typeColors.add(
                        findTypeColor(it.type.name.capitalizeFirstLetter())
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = state.sprites.frontDefault,
                        contentDescription = state.name,
                        modifier = Modifier
                            .size(200.dp)
                            .border(
                                brush = Brush.linearGradient(
                                    colors = if (typeColors.size <= 1) {
                                        listOf(typeColors[0], typeColors[0])
                                    } else typeColors
                                ),
                                width = 3.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (typeColors.size <= 1) {
                                        listOf(typeColors[0], typeColors[0])
                                    } else typeColors
                                ),
                                shape = RoundedCornerShape(8.dp),
                                alpha = 0.3f
                            )
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = state.name.capitalizeFirstLetter(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            item {
                Card {
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
                                text = state.id.toString(),
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
                                text = "${state.height / 10f} m",
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
                                text = "${state.weight / 10f} kg",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
            item {
                StatsCard(state.stats)
            }
            item {
                TypesCard(state.types)
            }
        }
    }
}


@Composable
private fun TypesCard(
    types: List<Type>
) {
    Card {
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
                    val capitalizedName = type.type.name.capitalizeFirstLetter()
                    PokemonTypeTag(
                        name = capitalizedName,
                        color = findTypeColor(capitalizedName),
                        textModifier = Modifier
                            .width(100.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsCard(
    stats: List<Stat>
) {
    Card {
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
                /* 0: hp, 1: Attack, 2: Defense, 3: Sp. at, 4: Sp. def, 5: Speed */
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ){
                    Text(
                        text = stats[0].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[0].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = stringResource(R.string.special_attack),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[3].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stats[1].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[1].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = stringResource(R.string.special_defense),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[4].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stats[2].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[4].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                    Text(
                        text = stats[5].stat.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stats[5].baseStat.toString(),
                        fontSize = 18.sp,
                    )
                }
            }
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
