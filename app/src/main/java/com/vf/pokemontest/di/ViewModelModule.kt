package com.vf.pokemontest.di

import com.vf.pokemontest.presentation.pokedex.PokedexViewModel
import com.vf.pokemontest.presentation.wishlist.WishlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PokedexViewModel(
            observePokemons = get(),
            refreshPokemons = get(),
            toggleWishlist = get(),
            errorMapper = get()
        )
    }

    viewModel {
        WishlistViewModel(
            observePokemons = get(),
            toggleWishlist = get()
        )
    }
}