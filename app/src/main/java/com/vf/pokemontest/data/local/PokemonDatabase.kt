package com.vf.pokemontest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vf.pokemontest.data.local.converters.ListConverters
import com.vf.pokemontest.data.local.dao.PokemonDao
import com.vf.pokemontest.data.local.dao.WishlistDao
import com.vf.pokemontest.data.local.entity.PokemonEntity
import com.vf.pokemontest.data.local.entity.WishlistEntity

@Database(
    entities = [
        PokemonEntity::class,
        WishlistEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListConverters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun wishlistDao(): WishlistDao

    companion object {
        const val DATABASE_NAME = "pokemon_app.db"
    }
}