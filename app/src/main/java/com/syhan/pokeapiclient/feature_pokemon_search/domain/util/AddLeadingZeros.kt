package com.syhan.pokeapiclient.feature_pokemon_search.domain.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun addLeadingZeros(number: Int): String {
    return String.format("%04d", number)
}