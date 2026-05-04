package com.nust.valentinegarage.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassmorphicSurface(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    // Note: True backdrop-blur requires specialized implementation in Compose.
    // This provides the visual approximation using semi-transparency and surface tinting.
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
            )
    ) {
        content()
    }
}
