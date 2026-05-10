package com.vf.pokemontest.presentation.wishlist

import com.vf.pokemontest.domain.model.PokemonListItem

data class WishlistState(
    val pokemons: List<PokemonListItem> = emptyList(),
    val searchQuery: String = ""
) {
    val filteredPokemons: List<PokemonListItem>
        get() = if (searchQuery.isBlank()) pokemons
        else pokemons.filter { it.matches(searchQuery) }

    val showEmptyWishlist: Boolean
        get() = pokemons.isEmpty()

    val showEmptySearchResult: Boolean
        get() = pokemons.isNotEmpty() && filteredPokemons.isEmpty() && searchQuery.isNotBlank()
}