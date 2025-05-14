package com.syhan.pokeapiclient.feature_pokemon_search.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.R

@Composable
fun ScrollUpAnimatedFAB(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = (isVisible),
        exit = slideOutVertically { it / 2 } + fadeOut(),
        enter = slideInVertically { it / 2 } + fadeIn()
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = RoundedCornerShape(40),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_up),
                contentDescription = stringResource(R.string.action_scroll_up)
            )
        }
    }
}

@Composable
fun RandomizeListAnimatedFAB(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = (isVisible),
        exit = slideOutHorizontally { it / 2 } + fadeOut(),
        enter = slideInHorizontally { it / 2 } + fadeIn()
    ) {
        ExtendedFloatingActionButton(
            text = {
                Text(
                    text = stringResource(R.string.randomize_list),
                    fontSize = 18.sp
                )
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_dice),
                    contentDescription = null
                )
            },
            onClick = onClick,
            shape = RoundedCornerShape(40)
        )
    }
}