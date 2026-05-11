package com.vf.pokemontest.domain

import app.cash.turbine.test
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonStats
import com.vf.pokemontest.domain.repository.PokemonRepository
import com.vf.pokemontest.domain.repository.WishlistRepository
import com.vf.pokemontest.domain.usecase.ObservePokemonsWithWishlistUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ObservePokemonsWithWishlistUseCaseTest {

    private val pokemonRepository: PokemonRepository = mockk()
    private val wishlistRepository: WishlistRepository = mockk()

    private val useCase = ObservePokemonsWithWishlistUseCase(
        pokemonRepository = pokemonRepository,
        wishlistRepository = wishlistRepository
    )

    @Test
    fun `combines pokemons and wishlist IDs into list items with correct flags`() = runTest {
        val pokemons = listOf(
            pokemon(1, "Bulbasaur"),
            pokemon(2, "Ivysaur"),
            pokemon(3, "Venusaur")
        )
        every { pokemonRepository.observeCachedPokemons() } returns flowOf(pokemons)
        every { wishlistRepository.observeWishlistIds() } returns flowOf(setOf(2))

        val result = useCase().first()

        assertEquals(3, result.size)
        assertFalse(result[0].isInWishlist)
        assertTrue(result[1].isInWishlist)
        assertFalse(result[2].isInWishlist)
    }

    @Test
    fun `wishlist change re-emits with updated flags`() = runTest {
        val pokemons = listOf(pokemon(1, "Bulbasaur"), pokemon(2, "Ivysaur"))
        val wishlistFlow = MutableStateFlow<Set<Int>>(emptySet())

        every { pokemonRepository.observeCachedPokemons() } returns flowOf(pokemons)
        every { wishlistRepository.observeWishlistIds() } returns wishlistFlow

        useCase().test {
            // Initial: nothing wishlisted
            val initial = awaitItem()
            assertFalse(initial[0].isInWishlist)
            assertFalse(initial[1].isInWishlist)

            // Toggle Bulbasaur into wishlist
            wishlistFlow.value = setOf(1)

            val updated = awaitItem()
            assertTrue(updated[0].isInWishlist)
            assertFalse(updated[1].isInWishlist)
        }
    }

    private fun pokemon(id: Int, name: String) = PokemonModel(
        id = id,
        name = name,
        imageUrl = null,
        types = listOf("Grass"),
        abilities = listOf("Overgrow"),
        stats = PokemonStats(45, 49, 49, 65, 65, 45)
    )
}