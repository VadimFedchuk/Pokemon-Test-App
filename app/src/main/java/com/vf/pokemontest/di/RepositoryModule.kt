package com.vf.pokemontest.di

import com.vf.pokemontest.data.repository.PokemonRepositoryImpl
import com.vf.pokemontest.data.repository.WishlistRepositoryImpl
import com.vf.pokemontest.domain.repository.PokemonRepository
import com.vf.pokemontest.domain.repository.WishlistRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<PokemonRepository> {
        PokemonRepositoryImpl(get(), get())
    }

    single<WishlistRepository> {
        WishlistRepositoryImpl(get())
    }
}