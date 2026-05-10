package com.vf.pokemontest.domain.usecase

import com.vf.pokemontest.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class ObserveWishlistCountUseCase(
    private val repository: WishlistRepository
) {
    operator fun invoke(): Flow<Int> = repository.observeCount()
}
