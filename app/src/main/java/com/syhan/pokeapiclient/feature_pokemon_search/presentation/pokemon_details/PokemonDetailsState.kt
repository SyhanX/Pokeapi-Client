package com.syhan.pokeapiclient.feature_pokemon_search.presentation.pokemon_details

import androidx.compose.runtime.Immutable
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Sprites
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Stat
import com.syhan.pokeapiclient.feature_pokemon_search.domain.model.Type

@Immutable
data class PokemonShortDetailsState(
    val id: Int = -1,
    val name: String = "",
    val sprites: Sprites = Sprites(
        frontDefault = null,
        frontShiny = null
    ),
    val types: List<Type> = emptyList(),
    val stats: List<Stat> = emptyList(),
)

@Immutable
data class PokemonFullDetailsState(
    val id: Int = -1,
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<Type> = emptyList(),
    val stats: List<Stat> = emptyList(),
    val sprites: Sprites = Sprites(
        frontDefault = null,
        frontShiny = null
    ),
)

/* index of each stat */
object PokemonStats {
    const val HP = 0
    const val ATTACK = 1
    const val DEFENSE = 2
    const val SPECIAL_ATTACK = 3
    const val SPECIAL_DEFENSE = 4
    const val SPEED = 5
}