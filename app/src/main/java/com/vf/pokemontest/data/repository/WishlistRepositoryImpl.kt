package com.vf.pokemontest.data.repository

import com.vf.pokemontest.data.local.dao.WishlistDao
import com.vf.pokemontest.data.local.entity.WishlistEntity
import com.vf.pokemontest.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun observeWishlistIds(): Flow<Set<Int>> =
        wishlistDao.observeWishlistIds().map { it.toSet() }

    override fun observeCount(): Flow<Int> =
        wishlistDao.observeCount()

    override suspend fun toggle(pokemonId: Int) {
        if (wishlistDao.isInWishlist(pokemonId)) {
            wishlistDao.remove(pokemonId)
        } else {
            wishlistDao.add(WishlistEntity(pokemonId))
        }
    }
}