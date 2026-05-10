package com.vf.pokemontest.data.repository

import com.vf.pokemontest.core.network.NetworkResult
import com.vf.pokemontest.core.network.safeApiCall
import com.vf.pokemontest.data.local.dao.PokemonDao
import com.vf.pokemontest.data.local.mapper.toDomain
import com.vf.pokemontest.data.local.mapper.toEntity
import com.vf.pokemontest.data.remote.api.PokemonApiService
import com.vf.pokemontest.data.remote.dto.PokemonDetailsDto
import com.vf.pokemontest.data.remote.mapper.toDomain
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonsLoadResult
import com.vf.pokemontest.domain.repository.PokemonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class PokemonRepositoryImpl(
    private val apiService: PokemonApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override fun observeCachedPokemons(): Flow<List<PokemonModel>> =
        pokemonDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Fetches the list of Pokémon names, then fans out 15 parallel detail requests.
     * Each detail call is wrapped in [safeApiCall] so individual failures don't bring
     * down the whole batch. Successful results are written to the cache atomically;
     * failed ones are reported via [PokemonsLoadResult.partialFailures].
     */
    override suspend fun refreshPokemons(): PokemonsLoadResult = coroutineScope {
        println("Started in: ${coroutineContext[Job]}")
        val listResponse = apiService.getPokemonList()
        val items = listResponse.results
        val requested = items.size

        val detailResults: List<NetworkResult<PokemonDetailsDto>> = items
            .map { item ->
                async { safeApiCall { apiService.getPokemonDetails(item.name) } }
            }
            .awaitAll()

        val pokemons: List<PokemonModel> = detailResults
            .filterIsInstance<NetworkResult.Success<PokemonDetailsDto>>()
            .map { it.data.toDomain() }
            .sortedBy { it.name.lowercase() }  // alphabetical, per spec

        val partialFailures = detailResults.count { it is NetworkResult.Error }

        if (pokemons.isEmpty() && requested > 0) {
            throw IOException("Failed to load Pokémon details")
        }

        pokemonDao.upsertAll(pokemons.map { it.toEntity() })

        PokemonsLoadResult(
            pokemons = pokemons,
            partialFailures = partialFailures,
            requested = requested
        )
    }
}