package com.vf.pokemontest.di

import androidx.room.Room
import com.vf.pokemontest.data.local.PokemonDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            PokemonDatabase::class.java,
            PokemonDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    single { get<PokemonDatabase>().pokemonDao() }
    single { get<PokemonDatabase>().wishlistDao() }
}