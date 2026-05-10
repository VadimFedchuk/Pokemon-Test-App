package com.vf.pokemontest.presentation.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vf.pokemontest.R
import com.vf.pokemontest.core.ui.theme.PokemonAppTheme
import com.vf.pokemontest.domain.model.PokemonListItem
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonStats
import com.vf.pokemontest.presentation.components.EmptyState
import com.vf.pokemontest.presentation.components.PokemonCard
import com.vf.pokemontest.presentation.components.PokemonSearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun WishlistScreen(
    viewModel: WishlistViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WishlistContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun WishlistContent(
    state: WishlistState,
    onIntent: (WishlistIntent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tab_wishlist),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        PokemonSearchBar(
            query = state.searchQuery,
            onQueryChange = { onIntent(WishlistIntent.SearchQueryChanged(it)) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.showEmptyWishlist -> EmptyState(
                icon = Icons.Outlined.HeartBroken,
                title = stringResource(R.string.empty_wishlist_title),
                subtitle = stringResource(R.string.empty_wishlist_subtitle)
            )

            state.showEmptySearchResult -> EmptyState(
                icon = Icons.Outlined.SearchOff,
                title = stringResource(R.string.empty_search_title),
                subtitle = stringResource(R.string.empty_search_subtitle)
            )

            else -> WishlistList(
                pokemons = state.filteredPokemons,
                onToggleWishlist = { id -> onIntent(WishlistIntent.ToggleWishlist(id)) }
            )
        }
    }
}

@Composable
private fun WishlistList(
    pokemons: List<PokemonListItem>,
    onToggleWishlist: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = pokemons,
            key = { it.pokemon.id }
        ) { item ->
            PokemonCard(
                item = item,
                onToggleWishlist = { onToggleWishlist(item.pokemon.id) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 800)
@Composable
private fun WishlistEmptyPreview() {
    PokemonAppTheme {
        WishlistContent(
            state = WishlistState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 800)
@Composable
private fun WishlistWithItemsPreview() {
    PokemonAppTheme {
        WishlistContent(
            state = WishlistState(
                pokemons = listOf(
                    PokemonListItem(
                        pokemon = PokemonModel(
                            id = 9,
                            name = "Blastoise",
                            imageUrl = null,
                            types = listOf("Water"),
                            abilities = listOf("Torrent", "Rain-Dish"),
                            stats = PokemonStats(79, 83, 100, 85, 105, 78)
                        ),
                        isInWishlist = true
                    )
                )
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 800)
@Composable
private fun WishlistEmptySearchPreview() {
    PokemonAppTheme {
        WishlistContent(
            state = WishlistState(
                pokemons = listOf(
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
                searchQuery = "xyz"
            ),
            onIntent = {}
        )
    }
}