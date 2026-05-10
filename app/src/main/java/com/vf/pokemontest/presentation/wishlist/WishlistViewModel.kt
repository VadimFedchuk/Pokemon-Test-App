package com.vf.pokemontest.presentation.wishlist

import androidx.lifecycle.viewModelScope
import com.vf.pokemontest.core.vm.BaseViewModel
import com.vf.pokemontest.domain.usecase.ObservePokemonsWithWishlistUseCase
import com.vf.pokemontest.domain.usecase.ToggleWishlistUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val observePokemons: ObservePokemonsWithWishlistUseCase,
    private val toggleWishlist: ToggleWishlistUseCase
) : BaseViewModel<WishlistState, WishlistIntent, WishlistEffect>(
    initialState = WishlistState()
) {

    init {
        observePokemons()
            .map { items -> items.filter { it.isInWishlist } }
            .onEach { wishlistItems ->
                updateState { copy(pokemons = wishlistItems) }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: WishlistIntent) {
        when (intent) {
            is WishlistIntent.SearchQueryChanged ->
                updateState { copy(searchQuery = intent.query) }
            is WishlistIntent.ToggleWishlist ->
                viewModelScope.launch { toggleWishlist(intent.pokemonId) }
        }
    }
}
