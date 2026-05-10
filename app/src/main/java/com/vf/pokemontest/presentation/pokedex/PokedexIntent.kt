package com.vf.pokemontest.presentation.pokedex

sealed interface PokedexIntent {
    data object LoadInitial : PokedexIntent
    data object Refresh : PokedexIntent
    data object Retry : PokedexIntent
    data class SearchQueryChanged(val query: String) : PokedexIntent
    data class ToggleWishlist(val pokemonId: Int) : PokedexIntent
}