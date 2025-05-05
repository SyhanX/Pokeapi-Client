package com.syhan.pokeapiclient.common.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.syhan.pokeapiclient.common.data.NavDestinations
import com.syhan.pokeapiclient.common.presentation.pokemon_details.PokemonDetailsScreen
import com.syhan.pokeapiclient.common.presentation.pokemon_list.PokemonListScreen

@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController()
) {
    AppContent(navController)
}

@Composable
fun AppContent(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavDestinations.PokemonListScreen
    ) {
        composable<NavDestinations.PokemonListScreen> {
            PokemonListScreen() {
                navController.navigate(NavDestinations.PokemonDetailsScreen)
            }
        }

        composable<NavDestinations.PokemonDetailsScreen> {
            PokemonDetailsScreen() {
                navController.navigate(NavDestinations.PokemonListScreen)
            }
        }
    }
}