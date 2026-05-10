package com.vf.pokemontest.core.ui.theme

import androidx.compose.ui.graphics.Color

data class PokemonTypeColor(
    val primary: Color,
    val background: Color,
    val onPrimary: Color = Color.White
)

object PokemonTypeColors {

    val Normal     = PokemonTypeColor(primary = Color(0xFFA8A77A), background = Color(0xFFF0F0E5))
    val Fire       = PokemonTypeColor(primary = Color(0xFFEE8130), background = Color(0xFFFCE5D2))
    val Water      = PokemonTypeColor(primary = Color(0xFF6390F0), background = Color(0xFFD7E2F9))
    val Electric   = PokemonTypeColor(primary = Color(0xFFF7D02C), background = Color(0xFFFCF2C5), onPrimary = Color(0xFF1F2937))
    val Grass      = PokemonTypeColor(primary = Color(0xFF7AC74C), background = Color(0xFFD8EFC7))
    val Ice        = PokemonTypeColor(primary = Color(0xFF96D9D6), background = Color(0xFFE2F4F3), onPrimary = Color(0xFF1F2937))
    val Fighting   = PokemonTypeColor(primary = Color(0xFFC22E28), background = Color(0xFFF1C6C4))
    val Poison     = PokemonTypeColor(primary = Color(0xFFA33EA1), background = Color(0xFFE8C9E7))
    val Ground     = PokemonTypeColor(primary = Color(0xFFE2BF65), background = Color(0xFFF5EBCD), onPrimary = Color(0xFF1F2937))
    val Flying     = PokemonTypeColor(primary = Color(0xFFA98FF3), background = Color(0xFFE5DCFB))
    val Psychic    = PokemonTypeColor(primary = Color(0xFFF95587), background = Color(0xFFFCCBDA))
    val Bug        = PokemonTypeColor(primary = Color(0xFFA6B91A), background = Color(0xFFE5EAB6), onPrimary = Color(0xFF1F2937))
    val Rock       = PokemonTypeColor(primary = Color(0xFFB6A136), background = Color(0xFFEAE2BD), onPrimary = Color(0xFF1F2937))
    val Ghost      = PokemonTypeColor(primary = Color(0xFF735797), background = Color(0xFFD7CDE3))
    val Dragon     = PokemonTypeColor(primary = Color(0xFF6F35FC), background = Color(0xFFD5C5FE))
    val Dark       = PokemonTypeColor(primary = Color(0xFF705746), background = Color(0xFFD8CFC9))
    val Steel      = PokemonTypeColor(primary = Color(0xFFB7B7CE), background = Color(0xFFE9E9F0), onPrimary = Color(0xFF1F2937))
    val Fairy      = PokemonTypeColor(primary = Color(0xFFD685AD), background = Color(0xFFF3D9E5), onPrimary = Color(0xFF1F2937))

    private val Default = Normal

    /**
     * Returns the colour set for the given type name (case-insensitive, matches API values).
     * Falls back to [Normal]-like neutral if the type is unknown — defensive against
     * potential new types in the API.
     */
    fun forType(typeName: String): PokemonTypeColor = when (typeName.lowercase()) {
        "normal"   -> Normal
        "fire"     -> Fire
        "water"    -> Water
        "electric" -> Electric
        "grass"    -> Grass
        "ice"      -> Ice
        "fighting" -> Fighting
        "poison"   -> Poison
        "ground"   -> Ground
        "flying"   -> Flying
        "psychic"  -> Psychic
        "bug"      -> Bug
        "rock"     -> Rock
        "ghost"    -> Ghost
        "dragon"   -> Dragon
        "dark"     -> Dark
        "steel"    -> Steel
        "fairy"    -> Fairy
        else       -> Default
    }
}
