package com.vf.pokemontest.presentation.pokedex

import com.vf.pokemontest.core.ui.UiText
import com.vf.pokemontest.domain.model.PokemonListItem

data class PokedexState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val pokemons: List<PokemonListItem> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: UiText? = null,
    val partialFailureCount: Int = 0,
    val totalRequested: Int = 0
) {
    val filteredPokemons: List<PokemonListItem>
        get() = if (searchQuery.isBlank()) pokemons
        else pokemons.filter { it.matches(searchQuery) }

    val showFullScreenLoading: Boolean
        get() = isLoading && pokemons.isEmpty()

    val showFullScreenError: Boolean
        get() = errorMessage != null && pokemons.isEmpty()

    val showPartialFailureBanner: Boolean
        get() = partialFailureCount > 0

    val showEmptySearchResult: Boolean
        get() = pokemons.isNotEmpty() && filteredPokemons.isEmpty() && searchQuery.isNotBlank()
}