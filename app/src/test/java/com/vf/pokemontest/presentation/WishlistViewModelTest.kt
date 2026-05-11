package com.vf.pokemontest.presentation

import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonListItem
import com.vf.pokemontest.domain.model.PokemonStats
import com.vf.pokemontest.domain.usecase.ObservePokemonsWithWishlistUseCase
import com.vf.pokemontest.domain.usecase.ToggleWishlistUseCase
import com.vf.pokemontest.presentation.wishlist.WishlistIntent
import com.vf.pokemontest.presentation.wishlist.WishlistViewModel
import com.vf.pokemontest.testutil.MainCoroutineRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val observePokemons: ObservePokemonsWithWishlistUseCase = mockk()
    private val toggleWishlist: ToggleWishlistUseCase = mockk(relaxed = true)

    private val pokemonsFlow = MutableStateFlow<List<PokemonListItem>>(emptyList())

    private fun buildViewModel(): WishlistViewModel {
        every { observePokemons() } returns pokemonsFlow
        return WishlistViewModel(
            observePokemons = observePokemons,
            toggleWishlist = toggleWishlist
        )
    }

    @Test
    fun `state - shows only wishlisted pokemons`() = runTest {
        pokemonsFlow.value = listOf(
            pokemonListItem(1, "Bulbasaur", inWishlist = false),
            pokemonListItem(2, "Ivysaur", inWishlist = true),
            pokemonListItem(3, "Venusaur", inWishlist = true)
        )

        val viewModel = buildViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.pokemons.size)
        assertEquals(listOf("Ivysaur", "Venusaur"), state.pokemons.map { it.pokemon.name })
    }

    @Test
    fun `search query - updates state immediately`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onIntent(WishlistIntent.SearchQueryChanged("char"))

        assertEquals("char", viewModel.state.value.searchQuery)
    }

    @Test
    fun `toggle wishlist - delegates to use case`() = runTest {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onIntent(WishlistIntent.ToggleWishlist(25))
        advanceUntilIdle()

        coVerify { toggleWishlist(25) }
    }

    private fun pokemonListItem(id: Int, name: String, inWishlist: Boolean) =
        PokemonListItem(
            pokemon = PokemonModel(
                id = id,
                name = name,
                imageUrl = null,
                types = listOf("Grass"),
                abilities = listOf("Overgrow"),
                stats = PokemonStats(45, 49, 49, 65, 65, 45)
            ),
            isInWishlist = inWishlist
        )
}