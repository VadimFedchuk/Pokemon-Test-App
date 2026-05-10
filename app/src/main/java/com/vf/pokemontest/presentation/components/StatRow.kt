package com.vf.pokemontest.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vf.pokemontest.core.ui.theme.PokemonAppTheme


@Composable
fun StatRow(
    label: String,
    value: Int,
    barColor: Color,
    modifier: Modifier = Modifier,
    maxValue: Int = 100
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(64.dp)
        )

        LinearProgressIndicator(
            progress = { (value.toFloat() / maxValue).coerceIn(0f, 1f) },
            color = barColor,
            trackColor = barColor.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            drawStopIndicator = {},
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
        )

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(36.dp)
                .padding(start = 12.dp)
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun StatRowPreview() {
    PokemonAppTheme {
        StatRow(
            label = "Attack",
            value = 90,
            barColor = Color(0xFF7AC74C),
            modifier = Modifier.padding(16.dp)
        )
    }
}