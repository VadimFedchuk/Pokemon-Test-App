package com.vf.pokemontest.domain.model

data class PokemonModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<String>,
    val abilities: List<String>,
    val stats: PokemonStats
)

data class PokemonStats(
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
) {
    val baseTotal: Int get() = hp + attack + defense + specialAttack + specialDefense + speed
}