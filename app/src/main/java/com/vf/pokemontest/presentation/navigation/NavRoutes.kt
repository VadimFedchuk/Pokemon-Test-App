package com.vf.pokemontest.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.ui.graphics.vector.ImageVector
import com.vf.pokemontest.R

sealed class NavRoutes(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: ImageVector
) {
    data object Pokedex : NavRoutes(
        route = "pokedex",
        labelResId = R.string.tab_pokedex,
        icon = Icons.Outlined.FormatListNumbered
    )

    data object Wishlist : NavRoutes(
        route = "wishlist",
        labelResId = R.string.tab_wishlist,
        icon = Icons.Outlined.Favorite
    )

    companion object {
        val bottomBarTabs: List<NavRoutes> = listOf(Pokedex, Wishlist)
    }
}