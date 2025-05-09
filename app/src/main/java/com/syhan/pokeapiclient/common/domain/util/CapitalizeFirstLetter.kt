package com.syhan.pokeapiclient.common.domain.util

fun String.capitalizeFirstLetter() : String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.uppercase()
        else it.toString()
    }
}