package com.syhan.pokeapiclient.feature_pokemon_search.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.feature_pokemon_search.data.PokemonSortingType

@Composable
fun SortingMenu(
    isExpanded: Boolean,
    isAscending: Boolean,
    onDismiss: () -> Unit,
    onSortingAlgSelect: (PokemonSortingType) -> Unit,
    onSortingOrderSelect: (Boolean) -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(15),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        offset = DpOffset(x = 0.dp, y = 14.dp)
    ) {
        PokemonSortingType.entries.forEach { algorithm ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(algorithm.selectedStatIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(algorithm.selectedStatName),
                        fontSize = 18.sp
                    )
                },
                onClick = { onSortingAlgSelect(algorithm) }
            )
        }
        HorizontalDivider()
        DropdownMenuItem(
            leadingIcon = {
                if (isAscending) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            },
            text = {
                Text(
                    text = "Ascending",
                    fontSize = 18.sp
                )
            },
            onClick = { onSortingOrderSelect(true) }
        )
        DropdownMenuItem(
            leadingIcon = {
                if (!isAscending) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            },
            text = {
                Text(
                    text = "Descending",
                    fontSize = 18.sp
                )
            },
            onClick = { onSortingOrderSelect(false) }
        )
    }
}

@Composable
fun SortingMenuBox(
    @StringRes statName: Int,
    @DrawableRes statIcon: Int,
    isMenuExpanded: Boolean,
    isSortingEnabled: Boolean,
    onClick: () -> Unit,
    menu: @Composable (() -> Unit)
) {
    val colors = MaterialTheme.colorScheme
    OutlinedTextField(
        colors = if (isMenuExpanded) {
            OutlinedTextFieldDefaults.colors(
                disabledContainerColor = colors.surfaceContainer,
                disabledTrailingIconColor = colors.secondary,
                disabledBorderColor = colors.onSurface,
                disabledLeadingIconColor = colors.onSurface
            )
        } else {
            OutlinedTextFieldDefaults.colors(
                disabledContainerColor = colors.surfaceContainerLow,
                disabledTrailingIconColor = colors.secondary,
                disabledBorderColor = colors.secondaryContainer,
                disabledLeadingIconColor = colors.secondary
            )
        },
        prefix = {
            Row {
                Text(
                    text = stringResource(R.string.sort_by) + " ",
                    fontSize = 18.sp,
                    color = colors.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(statName),
                    fontSize = 18.sp,
                    color = colors.onSurface
                )
            }
        },
        value = "",
        onValueChange = {},
        textStyle = TextStyle(
            color = colors.onSurface,
            fontSize = 18.sp
        ),
        enabled = false,
        leadingIcon = {
            Icon(
                painter = painterResource(statIcon),
                contentDescription = null,
            )
        },
        shape = RoundedCornerShape(30),
        trailingIcon = {
            if (isMenuExpanded) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.action_collapse)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.action_expand)
                )
            }
            menu()
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSortingEnabled) {
                    Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
    )
}