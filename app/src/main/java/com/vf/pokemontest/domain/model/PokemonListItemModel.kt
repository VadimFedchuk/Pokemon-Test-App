package com.vf.pokemontest.domain.model


data class PokemonListItem(
    val pokemon: PokemonModel,
    val isInWishlist: Boolean
) {

    /**
     * Returns true if this item matches the search query in any of:
     * name, any of the types, or any of the abilities.
     *
     * Comparison is case-insensitive and substring-based (per the spec:
     * "looking for text in the name, type, or abilities").
     * Empty / blank query matches everything.
     */
    fun matches(query: String): Boolean {
        if (query.isBlank()) return true
        val q = query.trim().lowercase()
        return pokemon.name.lowercase().contains(q) ||
                pokemon.types.any { it.lowercase().contains(q) } ||
                pokemon.abilities.any { it.lowercase().contains(q) }
    }
}