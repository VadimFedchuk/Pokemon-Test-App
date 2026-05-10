package com.vf.pokemontest.data.local.mapper

import com.vf.pokemontest.data.local.entity.PokemonEntity
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonStats

fun PokemonEntity.toDomain(): PokemonModel = PokemonModel(
    id = id,
    name = name,
    imageUrl = imageUrl,
    types = types,
    abilities = abilities,
    stats = PokemonStats(
        hp = hp,
        attack = attack,
        defense = defense,
        specialAttack = specialAttack,
        specialDefense = specialDefense,
        speed = speed
    )
)

fun PokemonModel.toEntity(): PokemonEntity = PokemonEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    types = types,
    abilities = abilities,
    hp = stats.hp,
    attack = stats.attack,
    defense = stats.defense,
    specialAttack = stats.specialAttack,
    specialDefense = stats.specialDefense,
    speed = stats.speed
)