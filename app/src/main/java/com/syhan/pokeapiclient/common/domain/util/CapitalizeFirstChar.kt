package com.syhan.pokeapiclient.common.domain.util

fun String.capitalizeFirstChar() : String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.uppercase()
        else it.toString()
    }
}