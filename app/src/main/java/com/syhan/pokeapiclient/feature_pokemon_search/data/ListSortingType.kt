package com.syhan.pokeapiclient.feature_pokemon_search.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.syhan.pokeapiclient.R

enum class ListSortingType(
    @StringRes val selectedStatName: Int,
    @DrawableRes val selectedStatIcon: Int,
) {
    SortByNumber(R.string.national_number, R.drawable.ic_id),
    SortByHp(R.string.hp, R.drawable.ic_heart),
    SortByAttack(R.string.attack, R.drawable.ic_sword),
    SortByDefense(R.string.defense, R.drawable.ic_shield)
}

