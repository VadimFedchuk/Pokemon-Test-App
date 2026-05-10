package com.vf.pokemontest.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vf.pokemontest.core.ui.theme.PokemonAppTheme


@Composable
fun AbilityChip(
    ability: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = ability,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.5.dp,
                color = accentColor,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 14.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
private fun AbilityChipPreview() {
    PokemonAppTheme {
        AbilityChip(ability = "Overgrow", accentColor = Color(0xFF7AC74C))
    }
}