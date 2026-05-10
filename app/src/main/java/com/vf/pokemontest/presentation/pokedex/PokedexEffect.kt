package com.vf.pokemontest.presentation.pokedex

import com.vf.pokemontest.core.ui.UiText

sealed interface PokedexEffect {
    data class ShowSnackbar(val message: UiText) : PokedexEffect
}