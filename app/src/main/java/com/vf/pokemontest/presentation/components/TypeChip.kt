package com.vf.pokemontest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vf.pokemontest.core.ui.theme.PokemonAppTheme
import com.vf.pokemontest.core.ui.theme.PokemonTypeColors

@Composable
fun TypeChip(
    type: String,
    modifier: Modifier = Modifier
) {
    val typeColor = PokemonTypeColors.forType(type)
    Text(
        text = type,
        style = MaterialTheme.typography.labelMedium,
        color = typeColor.onPrimary,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(typeColor.primary)
            .padding(horizontal = 14.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
private fun TypeChipPreview() {
    PokemonAppTheme {
        TypeChip(type = "Grass")
    }
}