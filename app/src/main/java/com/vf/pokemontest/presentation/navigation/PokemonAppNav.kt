package com.vf.pokemontest.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vf.pokemontest.domain.usecase.ObserveWishlistCountUseCase
import com.vf.pokemontest.presentation.pokedex.PokedexScreen
import com.vf.pokemontest.presentation.wishlist.WishlistScreen
import org.koin.compose.koinInject

@Composable
fun PokemonAppNav() {
    val observeWishlistCount = koinInject<ObserveWishlistCountUseCase>()
    val wishlistCount by observeWishlistCount().collectAsStateWithLifecycle(initialValue = 0)
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                wishlistBadgeCount = wishlistCount
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Pokedex.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = NavRoutes.Pokedex.route) {
                PokedexScreen()
            }

            composable(route = NavRoutes.Wishlist.route) {
                WishlistScreen()
            }
        }
    }
}