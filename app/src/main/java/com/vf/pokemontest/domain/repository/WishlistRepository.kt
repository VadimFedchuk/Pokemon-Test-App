package com.vf.pokemontest.domain.repository

import kotlinx.coroutines.flow.Flow

interface WishlistRepository {

    fun observeWishlistIds(): Flow<Set<Int>>

    fun observeCount(): Flow<Int>

    suspend fun toggle(pokemonId: Int)
}