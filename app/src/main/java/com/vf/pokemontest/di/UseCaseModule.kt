package com.vf.pokemontest.di

import com.vf.pokemontest.domain.usecase.ObservePokemonsWithWishlistUseCase
import com.vf.pokemontest.domain.usecase.ObserveWishlistCountUseCase
import com.vf.pokemontest.domain.usecase.RefreshPokemonsUseCase
import com.vf.pokemontest.domain.usecase.ToggleWishlistUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { RefreshPokemonsUseCase(get()) }
    factory { ObservePokemonsWithWishlistUseCase(get(), get()) }
    factory { ObserveWishlistCountUseCase(get()) }
    factory { ToggleWishlistUseCase(get()) }
}