package com.vf.pokemontest.presentation.wishlist

sealed interface WishlistIntent {
    data class SearchQueryChanged(val query: String) : WishlistIntent
    data class ToggleWishlist(val pokemonId: Int) : WishlistIntent
}