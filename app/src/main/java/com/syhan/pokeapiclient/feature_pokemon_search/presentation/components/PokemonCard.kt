package com.syhan.pokeapiclient.feature_pokemon_search.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import com.syhan.pokeapiclient.feature_pokemon_search.data.PokemonTypeColor
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type

private const val TAG = "PokemonCard"

@Composable
fun PokemonCard(
    name: String,
    sprite: String,
    types: List<Type>,
    onClick: () -> Unit,
) {
    Log.d(TAG, "PokemonCard: $types")
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .height(150.dp)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
            ) {
                Text(
                    text = name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .wrapContentWidth()
                ) {
                    types.forEach { type ->
                        val typeName = type.type.name.replaceFirstChar {
                            if (it.isLowerCase()) it.uppercase() else it.toString()
                        }
                        val typeColor = PokemonTypeColor.entries.find {
                            it.name == typeName
                        }?.color


                        PokemonTypeTag(
                            name = typeName,
                            color = Color(
                                color = typeColor ?: PokemonTypeColor.Unknown.color
                            )
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            AsyncImage(
                model = sprite,
                contentDescription = null,
                modifier = Modifier
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            Color.DarkGray
                        } else {
                            Color.White
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .size(150.dp)
            )

        }
    }
}

@PreviewLightDark
@Composable
private fun PokemonCardPreview() {
    PokeapiClientTheme {
        PokemonCard(
            name = "Pinkachu",
            sprite = "3",
            types = emptyList(),
            onClick = {}
        )
    }
}