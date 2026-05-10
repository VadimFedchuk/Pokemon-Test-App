package com.vf.pokemontest.data.remote.mapper

import com.vf.pokemontest.data.remote.dto.PokemonDetailsDto
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonStats

fun PokemonDetailsDto.toDomain(): PokemonModel {
    val statByName: Map<String, Int> = stats.associate { it.stat.name to it.baseStat }

    return PokemonModel(
        id = id,
        name = name.toDisplayName(),
        imageUrl = sprites.other?.officialArtwork?.frontDefault ?: sprites.frontDefault,
        types = types
            .sortedBy { it.slot }
            .map { it.type.name.toDisplayName() },
        abilities = abilities
            .sortedBy { it.slot }
            .map { it.ability.name.toDisplayName() },
        stats = PokemonStats(
            hp = statByName["hp"] ?: 0,
            attack = statByName["attack"] ?: 0,
            defense = statByName["defense"] ?: 0,
            specialAttack = statByName["special-attack"] ?: 0,
            specialDefense = statByName["special-defense"] ?: 0,
            speed = statByName["speed"] ?: 0
        )
    )
}

/**
 * Converts API-style lowercase-hyphenated strings to display titles.
 * "bulbasaur" → "Bulbasaur"; "rain-dish" → "Rain-Dish".
 */
private fun String.toDisplayName(): String =
    split('-').joinToString("-") { part ->
        part.replaceFirstChar { ch -> ch.titlecase() }
    }