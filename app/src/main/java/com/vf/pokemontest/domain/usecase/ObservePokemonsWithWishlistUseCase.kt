package com.vf.pokemontest.domain.usecase

import com.vf.pokemontest.domain.model.PokemonListItem
import com.vf.pokemontest.domain.repository.PokemonRepository
import com.vf.pokemontest.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Joins cached Pokémon data with the wishlist into UI-ready [PokemonListItem]s.
 *
 * Both source flows are reactive (Room emits on every table change), so the merged
 * stream re-emits whenever either side updates:
 *
 *   - User taps the heart on Pikachu       → wishlist table changes
 *                                           → wishlistIds Flow emits new Set
 *                                           → combine produces new list with isInWishlist updated for Pikachu
 *                                           → both Pokedex and Wishlist screens see the change
 *
 *   - Network refresh writes new pokemons  → pokemons table changes
 *                                           → cachedPokemons Flow emits new list
 *                                           → combine reapplies wishlist mapping over fresh data
 *
 * Two consumers (Pokedex and Wishlist ViewModels) share this single source of truth —
 * no manual sync, no race conditions, no possibility of one screen showing stale wishlist state.
 */
class ObservePokemonsWithWishlistUseCase(
    private val pokemonRepository: PokemonRepository,
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke(): Flow<List<PokemonListItem>> = combine(
        pokemonRepository.observeCachedPokemons(),
        wishlistRepository.observeWishlistIds()
    ) { pokemons, wishlistIds ->
        pokemons.map { pokemon ->
            PokemonListItem(
                pokemon = pokemon,
                isInWishlist = pokemon.id in wishlistIds
            )
        }
    }
}
