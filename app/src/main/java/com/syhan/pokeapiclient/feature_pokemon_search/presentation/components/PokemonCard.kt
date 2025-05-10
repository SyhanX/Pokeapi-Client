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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.syhan.pokeapiclient.common.domain.util.capitalizeFirstLetter
import com.syhan.pokeapiclient.common.presentation.theme.PokeapiClientTheme
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type
import com.syhan.pokeapiclient.feature_pokemon_search.domain.util.addLeadingZeros
import com.syhan.pokeapiclient.feature_pokemon_search.domain.util.findTypeColor

private const val TAG = "PokemonCard"

@Composable
fun PokemonCard(
    id: Int,
    name: String,
    sprite: String,
    types: List<Type>,
    onClick: () -> Unit,
) {
    Log.d(TAG, "PokemonCard: $types")
    Card(
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .height(120.dp)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "#${addLeadingZeros(id)}",
                        fontSize = 18.sp,
                        modifier = Modifier.alpha(0.75f)
                    )
                }
                Spacer(Modifier.weight(1f))
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .wrapContentWidth()
                ) {
                    types.forEach { type ->
                        val typeProperName = type.type.name.capitalizeFirstLetter()

                        PokemonTypeTag(
                            name = typeProperName,
                            color = findTypeColor(typeProperName)
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
                    .size(120.dp)
            )

        }
    }
}

@PreviewLightDark
@Composable
private fun PokemonCardPreview() {
    PokeapiClientTheme {
        PokemonCard(
            id = 2,
            name = "Pinkachu",
            sprite = "3",
            types = emptyList(),
            onClick = {}
        )
    }
}