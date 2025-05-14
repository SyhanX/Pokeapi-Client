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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syhan.pokeapiclient.R
import com.syhan.pokeapiclient.feature_pokemon_search.data.ListSortingType

@Composable
fun ChooseSortingDropdownMenu(
    isMenuExpanded: Boolean,
    isAscending: Boolean,
    onDismissRequest: () -> Unit,
    onSortingTypeSelect: (ListSortingType) -> Unit,
    onSortingOrderSelect: (isAscending: Boolean) -> Unit,
) {
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(15),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        offset = DpOffset(x = 0.dp, y = 14.dp)
    ) {
        ListSortingType.entries.forEach { type ->
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(type.selectedStatIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(type.selectedStatName),
                        fontSize = 18.sp
                    )
                },
                onClick = { onSortingTypeSelect(type) }
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
                    text = stringResource(R.string.ascending),
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
                    text = stringResource(R.string.descending),
                    fontSize = 18.sp
                )
            },
            onClick = { onSortingOrderSelect(false) }
        )
    }
}

@Composable
fun ChooseSortingDropdownBox(
    @StringRes statName: Int,
    @DrawableRes statIcon: Int,
    isMenuExpanded: Boolean,
    isSortingEnabled: Boolean,
    onClick: () -> Unit,
    dropdownMenu: @Composable (() -> Unit)
) {
    val color = MaterialTheme.colorScheme
    OutlinedTextField(
        colors = if (isMenuExpanded) {
            OutlinedTextFieldDefaults.colors(
                disabledContainerColor = color.surfaceContainer,
                disabledTrailingIconColor = color.secondary,
                disabledBorderColor = color.onSurface,
                disabledLeadingIconColor = color.onSurface
            )
        } else {
            OutlinedTextFieldDefaults.colors(
                disabledContainerColor = color.surfaceContainerLow,
                disabledTrailingIconColor = color.secondary,
                disabledBorderColor = color.secondaryContainer,
                disabledLeadingIconColor = color.secondary
            )
        },
        prefix = {
            Row {
                Text(
                    text = stringResource(R.string.sort_by) + " ",
                    fontSize = 18.sp,
                    color = color.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(statName),
                    fontSize = 18.sp,
                    color = color.onSurface
                )
            }
        },
        value = "",
        onValueChange = {},
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
            dropdownMenu()
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