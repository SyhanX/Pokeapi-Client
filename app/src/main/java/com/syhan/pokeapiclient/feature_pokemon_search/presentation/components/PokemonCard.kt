package com.syhan.pokeapiclient.feature_pokemon_search.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.syhan.pokeapiclient.feature_pokemon_search.data.ListSortingType
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type
import com.syhan.pokeapiclient.feature_pokemon_search.domain.util.addLeadingZeros
import com.syhan.pokeapiclient.feature_pokemon_search.data.findTypeColor

private const val TAG = "PokemonCard"

@Composable
fun PokemonCard(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    hpValue: Int,
    attackValue: Int,
    defenseValue: Int,
    sprite: String,
    types: List<Type>,
    sortingType: ListSortingType,
    onClick: () -> Unit,
) {
    val typeColors = mutableListOf<Color>()
    types.forEach {
        typeColors.add(
            findTypeColor(it.type.name)
        )
    }
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = sprite,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .border(
                        brush = Brush.linearGradient(
                            colors = if (typeColors.size <= 1) {
                                listOf(typeColors[0], typeColors[0])
                            } else typeColors
                        ),
                        width = 2.dp,
                        shape = RoundedCornerShape(20)
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (typeColors.size <= 1) {
                                listOf(typeColors[0], typeColors[0])
                            } else typeColors
                        ),
                        shape = RoundedCornerShape(20),
                        alpha = 0.15f
                    )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .height(120.dp)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 12.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = name,
                        fontSize = 22.sp,
                        maxLines = 2,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = when (sortingType) {
                            ListSortingType.SortByNumber -> "#${addLeadingZeros(id)}"
                            ListSortingType.SortByHp -> "$hpValue HP"
                            ListSortingType.SortByAttack -> "$attackValue Atk."
                            ListSortingType.SortByDefense -> "$defenseValue Def."
                        },
                        fontSize = 18.sp,
                        modifier = Modifier
                            .alpha(0.6f)
                            .weight(1f)
                    )
                }
                Spacer(Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    types.forEach { type ->
                        PokemonTypeTag(
                            name = type.type.name,
                            color = findTypeColor(type.type.name)
                        )
                    }
                }
            }
        }
    }
}