package com.vf.pokemontest.data

import com.vf.pokemontest.data.local.dao.PokemonDao
import com.vf.pokemontest.data.local.entity.PokemonEntity
import com.vf.pokemontest.data.remote.api.PokemonApiService
import com.vf.pokemontest.data.remote.dto.NamedApiResource
import com.vf.pokemontest.data.remote.dto.OfficialArtworkDto
import com.vf.pokemontest.data.remote.dto.OtherSpritesDto
import com.vf.pokemontest.data.remote.dto.PokemonAbilitySlotDto
import com.vf.pokemontest.data.remote.dto.PokemonDetailsDto
import com.vf.pokemontest.data.remote.dto.PokemonListItemDto
import com.vf.pokemontest.data.remote.dto.PokemonListResponseDto
import com.vf.pokemontest.data.remote.dto.PokemonStatDto
import com.vf.pokemontest.data.remote.dto.PokemonTypeSlotDto
import com.vf.pokemontest.data.remote.dto.SpritesDto
import com.vf.pokemontest.data.repository.PokemonRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PokemonRepositoryImplTest {

    private val apiService: PokemonApiService = mockk()
    private val pokemonDao: PokemonDao = mockk(relaxed = true)

    private val repository = PokemonRepositoryImpl(apiService, pokemonDao)

    @Test
    fun `refresh - all details succeed - returns full result and writes cache`() = runTest {
        coEvery { apiService.getPokemonList(any(), any()) } returns listResponse(
            "bulbasaur", "ivysaur"
        )
        coEvery { apiService.getPokemonDetails("bulbasaur") } returns
                pokemonDetailsDto(id = 1, name = "bulbasaur")
        coEvery { apiService.getPokemonDetails("ivysaur") } returns
                pokemonDetailsDto(id = 2, name = "ivysaur")

        val result = repository.refreshPokemons()

        assertEquals(2, result.pokemons.size)
        assertEquals(0, result.partialFailures)
        assertEquals(2, result.requested)
        coVerify { pokemonDao.upsertAll(match { it.size == 2 }) }
    }

    @Test
    fun `refresh - some details fail - returns partial result`() = runTest {
        coEvery { apiService.getPokemonList(any(), any()) } returns listResponse(
            "bulbasaur", "ivysaur"
        )
        coEvery { apiService.getPokemonDetails("bulbasaur") } returns
                pokemonDetailsDto(id = 1, name = "bulbasaur")
        coEvery { apiService.getPokemonDetails("ivysaur") } throws IOException("network")

        val result = repository.refreshPokemons()

        assertEquals(1, result.pokemons.size)
        assertEquals(1, result.partialFailures)
        assertEquals(2, result.requested)
        coVerify { pokemonDao.upsertAll(match { it.size == 1 }) }
    }

    @Test
    fun `refresh - all details fail - throws IOException defensively`() = runTest {
        coEvery { apiService.getPokemonList(any(), any()) } returns listResponse(
            "bulbasaur", "ivysaur"
        )
        coEvery { apiService.getPokemonDetails(any()) } throws IOException("network")

        assertFailsWith<IOException> {
            repository.refreshPokemons()
        }
    }

    @Test
    fun `refresh - list endpoint fails - exception propagates`() = runTest {
        coEvery { apiService.getPokemonList(any(), any()) } throws IOException("network")

        assertFailsWith<IOException> {
            repository.refreshPokemons()
        }
        // Details should never be called
        coVerify(exactly = 0) { apiService.getPokemonDetails(any()) }
    }

    @Test
    fun `observeCachedPokemons - maps entities to domain models`() = runTest {
        every { pokemonDao.observeAll() } returns flowOf(
            listOf(
                PokemonEntity(
                    id = 1,
                    name = "Bulbasaur",
                    imageUrl = null,
                    types = listOf("Grass"),
                    abilities = listOf("Overgrow"),
                    hp = 45, attack = 49, defense = 49,
                    specialAttack = 65, specialDefense = 65, speed = 45
                )
            )
        )

        val result = repository.observeCachedPokemons().first()

        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result[0].name)
        assertEquals(45, result[0].stats.hp)
    }

    private fun listResponse(vararg names: String) = PokemonListResponseDto(
        count = names.size,
        next = null,
        previous = null,
        results = names.map { PokemonListItemDto(name = it, url = "https://pokeapi/$it") }
    )

    private fun pokemonDetailsDto(
        id: Int = 1,
        name: String = "bulbasaur",
        types: List<String> = listOf("grass"),
        abilities: List<String> = listOf("overgrow"),
        hp: Int = 45,
        attack: Int = 49,
        defense: Int = 49,
        specialAttack: Int = 65,
        specialDefense: Int = 65,
        speed: Int = 45
    ): PokemonDetailsDto = PokemonDetailsDto(
        id = id,
        name = name,
        sprites = SpritesDto(
            frontDefault = "https://sprite.png",
            other = OtherSpritesDto(
                officialArtwork = OfficialArtworkDto(frontDefault = "https://artwork.png")
            )
        ),
        types = types.mapIndexed { index, type ->
            PokemonTypeSlotDto(
                slot = index + 1,
                type = NamedApiResource(name = type, url = "")
            )
        },
        abilities = abilities.mapIndexed { index, ability ->
            PokemonAbilitySlotDto(
                ability = NamedApiResource(name = ability, url = ""),
                isHidden = false,
                slot = index + 1
            )
        },
        stats = listOf(
            PokemonStatDto(hp, 0, NamedApiResource("hp", "")),
            PokemonStatDto(attack, 0, NamedApiResource("attack", "")),
            PokemonStatDto(defense, 0, NamedApiResource("defense", "")),
            PokemonStatDto(specialAttack, 0, NamedApiResource("special-attack", "")),
            PokemonStatDto(specialDefense, 0, NamedApiResource("special-defense", "")),
            PokemonStatDto(speed, 0, NamedApiResource("speed", ""))
        )
    )
}