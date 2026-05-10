package com.vf.pokemontest.di

import com.vf.pokemontest.core.util.ErrorMapper
import org.koin.dsl.module

val coreModule = module {
    single { ErrorMapper() }
}