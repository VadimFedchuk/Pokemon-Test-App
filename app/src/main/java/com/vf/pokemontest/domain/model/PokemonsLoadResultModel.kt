package com.vf.pokemontest.domain.model

data class PokemonsLoadResult(
    val pokemons: List<PokemonModel>,
    val partialFailures: Int,
    val requested: Int
)