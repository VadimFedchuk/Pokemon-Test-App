package com.vf.pokemontest.presentation.pokedex

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vf.pokemontest.R
import com.vf.pokemontest.core.ui.UiText
import com.vf.pokemontest.core.ui.theme.PokemonAppTheme
import com.vf.pokemontest.domain.model.PokemonListItem
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonStats
import com.vf.pokemontest.presentation.components.EmptyState
import com.vf.pokemontest.presentation.components.ErrorView
import com.vf.pokemontest.presentation.components.PartialFailureBanner
import com.vf.pokemontest.presentation.components.PokemonCard
import com.vf.pokemontest.presentation.components.PokemonSearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun PokedexScreen(
    viewModel: PokedexViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PokedexEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message.asString(context))
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        PokedexContent(
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun PokedexContent(
    state: PokedexState,
    onIntent: (PokedexIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tab_pokedex),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        PokemonSearchBar(
            query = state.searchQuery,
            onQueryChange = { onIntent(PokedexIntent.SearchQueryChanged(it)) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.showFullScreenLoading -> CenteredLoading()

            state.showFullScreenError -> ErrorView(
                message = state.errorMessage?.asString().orEmpty(),
                onRetry = { onIntent(PokedexIntent.Retry) }
            )

            else -> PokedexListWithRefresh(state = state, onIntent = onIntent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokedexListWithRefresh(
    state: PokedexState,
    onIntent: (PokedexIntent) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onIntent(PokedexIntent.Refresh) },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.showPartialFailureBanner) {
                item(key = "partial-failure-banner") {
                    PartialFailureBanner(
                        message = stringResource(
                            R.string.partial_failure_banner,
                            state.partialFailureCount,
                            state.totalRequested
                        )
                    )
                }
            }

            if (state.showEmptySearchResult) {
                item(key = "empty-search-result") {
                    EmptyState(
                        icon = Icons.Outlined.SearchOff,
                        title = stringResource(R.string.empty_search_title),
                        subtitle = stringResource(R.string.empty_search_subtitle),
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            } else {
                items(
                    items = state.filteredPokemons,
                    key = { it.pokemon.id }
                ) { item ->
                    PokemonCard(
                        item = item,
                        onToggleWishlist = {
                            onIntent(PokedexIntent.ToggleWishlist(item.pokemon.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 800)
@Composable
private fun PokedexLoadingPreview() {
    PokemonAppTheme {
        PokedexContent(
            state = PokedexState(isLoading = true),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 800)
@Composable
private fun PokedexErrorPreview() {
    PokemonAppTheme {
        PokedexContent(
            state = PokedexState(
                errorMessage = UiText.Dynamic("No internet connection.")
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 1000)
@Composable
private fun PokedexWithItemsPreview() {
    PokemonAppTheme {
        PokedexContent(
            state = PokedexState(
                pokemons = listOf(
                    PokemonListItem(
                        pokemon = PokemonModel(
                            id = 1,
                            name = "Bulbasaur",
                            imageUrl = null,
                            types = listOf("Grass", "Poison"),
                            abilities = listOf("Overgrow"),
                            stats = PokemonStats(45, 49, 49, 65, 65, 45)
                        ),
                        isInWishlist = false
                    ),
                    PokemonListItem(
                        pokemon = PokemonModel(
                            id = 9,
                            name = "Blastoise",
                            imageUrl = null,
                            types = listOf("Water"),
                            abilities = listOf("Torrent"),
                            stats = PokemonStats(79, 83, 100, 85, 105, 78)
                        ),
                        isInWishlist = true
                    )
                ),
                partialFailureCount = 2,
                totalRequested = 15
            ),
            onIntent = {}
        )
    }
}