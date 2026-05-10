package com.vf.pokemontest.data.remote.dto

import com.squareup.moshi.Json

data class PokemonDetailsDto(
    val id: Int,
    val name: String,
    val sprites: SpritesDto,
    val types: List<PokemonTypeSlotDto>,
    val abilities: List<PokemonAbilitySlotDto>,
    val stats: List<PokemonStatDto>
)

data class SpritesDto(
    @Json(name = "front_default") val frontDefault: String?,
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    /** `official-artwork` is a high-quality render — what we want to display. */
    @Json(name = "official-artwork") val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @Json(name = "front_default") val frontDefault: String?
)

data class PokemonTypeSlotDto(
    /** Order of the type on the Pokémon. We sort by slot to get primary type first. */
    val slot: Int,
    val type: NamedApiResource
)

data class PokemonAbilitySlotDto(
    val ability: NamedApiResource,
    @Json(name = "is_hidden") val isHidden: Boolean,
    val slot: Int
)

data class PokemonStatDto(
    @Json(name = "base_stat") val baseStat: Int,
    val effort: Int,
    val stat: NamedApiResource
)

/**
 * PokeAPI uses {name, url} pairs to reference any related resource (type, ability, stat,
 * species, etc.). We only consume `name`; `url` would let us follow the link if we
 * needed deeper data, which we don't.
 */
data class NamedApiResource(
    val name: String,
    val url: String
)
