package com.vf.pokemontest.domain.usecase

import com.vf.pokemontest.domain.model.PokemonsLoadResult
import com.vf.pokemontest.domain.repository.PokemonRepository

class RefreshPokemonsUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): PokemonsLoadResult = repository.refreshPokemons()
}