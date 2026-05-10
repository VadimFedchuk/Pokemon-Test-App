package com.vf.pokemontest.domain.usecase

import com.vf.pokemontest.domain.repository.WishlistRepository

class ToggleWishlistUseCase(
    private val repository: WishlistRepository
) {
    suspend operator fun invoke(pokemonId: Int) = repository.toggle(pokemonId)
}