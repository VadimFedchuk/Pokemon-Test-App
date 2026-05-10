package com.vf.pokemontest.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        coreModule,
        viewModelModule
    )
}