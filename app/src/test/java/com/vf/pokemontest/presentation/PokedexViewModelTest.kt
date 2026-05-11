package com.vf.pokemontest.presentation

import app.cash.turbine.test
import com.vf.pokemontest.core.ui.UiText
import com.vf.pokemontest.core.util.ErrorMapper
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonListItem
import com.vf.pokemontest.domain.model.PokemonStats
import com.vf.pokemontest.domain.model.PokemonsLoadResult
import com.vf.pokemontest.domain.usecase.ObservePokemonsWithWishlistUseCase
import com.vf.pokemontest.domain.usecase.RefreshPokemonsUseCase
import com.vf.pokemontest.domain.usecase.ToggleWishlistUseCase
import com.vf.pokemontest.presentation.pokedex.PokedexEffect
import com.vf.pokemontest.presentation.pokedex.PokedexIntent
import com.vf.pokemontest.presentation.pokedex.PokedexViewModel
import com.vf.pokemontest.testutil.MainCoroutineRule
import io.mockk.coEvery
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val observePokemons: ObservePokemonsWithWishlistUseCase = mockk()
    private val refreshPokemons: RefreshPokemonsUseCase = mockk()
    private val toggleWishlist: ToggleWishlistUseCase = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()

    private val pokemonsFlow = MutableStateFlow<List<PokemonListItem>>(emptyList())

    private fun buildViewModel(): PokedexViewModel {
        every { observePokemons() } returns pokemonsFlow
        return PokedexViewModel(
            observePokemons = observePokemons,
            refreshPokemons = refreshPokemons,
            toggleWishlist = toggleWishlist,
            errorMapper = errorMapper
        )
    }

    @Test
    fun `initial load - success - clears loading and populates pokemons`() = runTest {
        val items = listOf(pokemonListItem(1, "Bulbasaur"))
        pokemonsFlow.value = items
        coEvery { refreshPokemons() } returns PokemonsLoadResult(
            pokemons = items.map { it.pokemon },
            partialFailures = 0,
            requested = items.size
        )

        val viewModel = buildViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(items, state.pokemons)
        assertEquals(false, state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `initial load - failure with no cache - shows full screen error`() = runTest {
        val exception = RuntimeException("network")
        coEvery { refreshPokemons() } throws exception
        every { errorMapper.map(exception) } returns UiText.Dynamic("error")

        val viewModel = buildViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(UiText.Dynamic("error"), state.errorMessage)
        assertTrue(state.showFullScreenError)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `refresh - failure with existing data - emits snackbar effect and keeps list`() = runTest {
        // Initial load — success with data
        val items = listOf(pokemonListItem(1, "Bulbasaur"))
        pokemonsFlow.value = items
        coEvery { refreshPokemons() } returns PokemonsLoadResult(items.map { it.pokemon }, 0, 1)

        val viewModel = buildViewModel()
        advanceUntilIdle()

        // Now refresh fails
        val exception = RuntimeException("network")
        coEvery { refreshPokemons() } throws exception
        every { errorMapper.map(exception) } returns UiText.Dynamic("error")

        viewModel.effect.test {
            viewModel.onIntent(PokedexIntent.Refresh)
            advanceUntilIdle()

            val effect = awaitItem()
            assertEquals(PokedexEffect.ShowSnackbar(UiText.Dynamic("error")), effect)
        }

        // Data still on screen, no full-screen error
        val state = viewModel.state.value
        assertEquals(items, state.pokemons)
        assertNull(state.errorMessage)
    }

    @Test
    fun `partial failure - sets banner counts in state`() = runTest {
        val items = listOf(pokemonListItem(1, "Bulbasaur"))
        pokemonsFlow.value = items
        coEvery { refreshPokemons() } returns PokemonsLoadResult(
            pokemons = items.map { it.pokemon },
            partialFailures = 3,
            requested = 15
        )

        val viewModel = buildViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(3, state.partialFailureCount)
        assertEquals(15, state.totalRequested)
        assertTrue(state.showPartialFailureBanner)
    }

    @Test
    fun `search query changed - updates state immediately`() = runTest {
        coEvery { refreshPokemons() } returns PokemonsLoadResult(emptyList(), 0, 0)

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onIntent(PokedexIntent.SearchQueryChanged("char"))

        assertEquals("char", viewModel.state.value.searchQuery)
    }

    @Test
    fun `toggle wishlist - delegates to use case with correct id`() = runTest {
        coEvery { refreshPokemons() } returns PokemonsLoadResult(emptyList(), 0, 0)

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onIntent(PokedexIntent.ToggleWishlist(25))
        advanceUntilIdle()

        coVerify { toggleWishlist(25) }
    }

    private fun pokemonListItem(id: Int, name: String, inWishlist: Boolean = false) =
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