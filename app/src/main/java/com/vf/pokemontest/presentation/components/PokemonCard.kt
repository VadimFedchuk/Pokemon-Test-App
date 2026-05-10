package com.vf.pokemontest.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.vf.pokemontest.R
import com.vf.pokemontest.core.ui.theme.PokemonAppTheme
import com.vf.pokemontest.core.ui.theme.PokemonTypeColors
import com.vf.pokemontest.domain.model.PokemonModel
import com.vf.pokemontest.domain.model.PokemonListItem
import com.vf.pokemontest.domain.model.PokemonStats

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonCard(
    item: PokemonListItem,
    onToggleWishlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pokemon = item.pokemon
    val primaryType = pokemon.types.firstOrNull() ?: "normal"
    val typeColor = PokemonTypeColors.forType(primaryType)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = typeColor.background),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                PokemonSpriteCircle(imageUrl = pokemon.imageUrl)

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pokemon.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        pokemon.types.forEach { type -> TypeChip(type = type) }
                    }

                    if (pokemon.abilities.isNotEmpty()) {
                        Spacer(Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            pokemon.abilities.forEach { ability ->
                                AbilityChip(ability = ability, accentColor = typeColor.primary)
                            }
                        }
                    }
                }

                HeartButton(
                    isInWishlist = item.isInWishlist,
                    onClick = onToggleWishlist
                )
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = typeColor.primary.copy(alpha = 0.25f))
            Spacer(Modifier.height(12.dp))

            // Stats
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                StatRow(stringResource(R.string.stat_hp), pokemon.stats.hp, typeColor.primary)
                StatRow(stringResource(R.string.stat_attack), pokemon.stats.attack, typeColor.primary)
                StatRow(stringResource(R.string.stat_defense), pokemon.stats.defense, typeColor.primary)
                StatRow(stringResource(R.string.stat_special_attack), pokemon.stats.specialAttack, typeColor.primary)
                StatRow(stringResource(R.string.stat_special_defense), pokemon.stats.specialDefense, typeColor.primary)
                StatRow(stringResource(R.string.stat_speed), pokemon.stats.speed, typeColor.primary)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.stat_base_total, pokemon.stats.baseTotal),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PokemonSpriteCircle(imageUrl: String?) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.cd_pokemon_sprite),
            contentScale = ContentScale.Fit,
            loading = { SpritePlaceholder() },
            error = { SpritePlaceholder() },
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
private fun SpritePlaceholder() {
    Icon(
        imageVector = Icons.Filled.Image,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.size(40.dp)
    )
}

@Composable
private fun HeartButton(
    isInWishlist: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isInWishlist) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heart-scale"
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.6f))
    ) {
        AnimatedContent(
            targetState = isInWishlist,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "heart-icon"
        ) { liked ->
            Icon(
                imageVector = if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = stringResource(
                    if (liked) R.string.cd_remove_from_wishlist else R.string.cd_add_to_wishlist
                ),
                tint = if (liked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.scale(scale)
            )
        }
    }
}

// region Previews

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 480)
@Composable
private fun PokemonCardPreview() {
    PokemonAppTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            PokemonCard(
                item = PokemonListItem(
                    pokemon = previewBulbasaur(),
                    isInWishlist = false
                ),
                onToggleWishlist = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, heightDp = 480)
@Composable
private fun PokemonCardWishlistedPreview() {
    PokemonAppTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            PokemonCard(
                item = PokemonListItem(
                    pokemon = previewBlastoise(),
                    isInWishlist = true
                ),
                onToggleWishlist = {}
            )
        }
    }
}

private fun previewBulbasaur() = PokemonModel(
    id = 1,
    name = "Bulbasaur",
    imageUrl = null,
    types = listOf("Grass", "Poison"),
    abilities = listOf("Overgrow", "Chlorophyll"),
    stats = PokemonStats(45, 49, 49, 65, 65, 45)
)

private fun previewBlastoise() = PokemonModel(
    id = 9,
    name = "Blastoise",
    imageUrl = null,
    types = listOf("Water"),
    abilities = listOf("Torrent", "Rain-Dish"),
    stats = PokemonStats(79, 83, 100, 85, 105, 78)
)