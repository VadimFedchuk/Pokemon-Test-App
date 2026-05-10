package com.vf.pokemontest.data.remote.api

import com.vf.pokemontest.core.network.ApiConstants
import com.vf.pokemontest.data.remote.dto.PokemonDetailsDto
import com.vf.pokemontest.data.remote.dto.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = ApiConstants.POKEMON_LIMIT,
        @Query("offset") offset: Int = 0
    ): PokemonListResponseDto

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): PokemonDetailsDto
}