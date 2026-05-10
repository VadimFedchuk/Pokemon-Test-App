package com.vf.pokemontest.presentation.pokedex

import androidx.lifecycle.viewModelScope
import com.vf.pokemontest.core.util.ErrorMapper
import com.vf.pokemontest.core.vm.BaseViewModel
import com.vf.pokemontest.domain.usecase.ObservePokemonsWithWishlistUseCase
import com.vf.pokemontest.domain.usecase.RefreshPokemonsUseCase
import com.vf.pokemontest.domain.usecase.ToggleWishlistUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PokedexViewModel(
    private val observePokemons: ObservePokemonsWithWishlistUseCase,
    private val refreshPokemons: RefreshPokemonsUseCase,
    private val toggleWishlist: ToggleWishlistUseCase,
    private val errorMapper: ErrorMapper
) : BaseViewModel<PokedexState, PokedexIntent, PokedexEffect>(
    initialState = PokedexState()
) {

    init {
        observePokemons()
            .onEach { items -> updateState { copy(pokemons = items) } }
            .launchIn(viewModelScope)

        onIntent(PokedexIntent.LoadInitial)
    }

    override fun onIntent(intent: PokedexIntent) {
        when (intent) {
            PokedexIntent.LoadInitial -> loadInitial()
            PokedexIntent.Retry -> loadInitial()
            PokedexIntent.Refresh -> refresh()
            is PokedexIntent.SearchQueryChanged ->
                updateState { copy(searchQuery = intent.query) }

            is PokedexIntent.ToggleWishlist ->
                viewModelScope.launch { toggleWishlist(intent.pokemonId) }
        }
    }

    private fun loadInitial() {
        updateState {
            copy(
                isLoading = pokemons.isEmpty(),
                errorMessage = null
            )
        }
        fetchFromNetwork()
    }

    private fun refresh() {
        if (currentState.isRefreshing) return

        updateState { copy(isRefreshing = true, errorMessage = null) }
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        viewModelScope.launch {
            runCatching { refreshPokemons() }
                .onSuccess { result ->
                    updateState {
                        copy(
                            isLoading = false,
                            isRefreshing = false,
                            partialFailureCount = result.partialFailures,
                            totalRequested = result.requested,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val mapped = errorMapper.map(throwable)
                    val hadData = currentState.pokemons.isNotEmpty()

                    updateState {
                        copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = if (hadData) null else mapped
                        )
                    }

                    if (hadData) {
                        sendEffect(PokedexEffect.ShowSnackbar(mapped))
                    }
                }
        }
    }
}