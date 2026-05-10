package com.vf.pokemontest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vf.pokemontest.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT pokemonId FROM wishlist")
    fun observeWishlistIds(): Flow<List<Int>>

    @Query("SELECT COUNT(*) FROM wishlist")
    fun observeCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE pokemonId = :id)")
    suspend fun isInWishlist(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(entity: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE pokemonId = :id")
    suspend fun remove(id: Int)
}