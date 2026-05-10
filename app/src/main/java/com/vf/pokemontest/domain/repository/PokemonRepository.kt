package com.vf.pokemontest.domain.repository

import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonsLoadResult
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun observeCachedPokemons(): Flow<List<PokemonModel>>

    suspend fun refreshPokemons(): PokemonsLoadResult
}