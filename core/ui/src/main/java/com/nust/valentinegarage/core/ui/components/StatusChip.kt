package com.nust.valentinegarage.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nust.valentinegarage.core.ui.theme.SuccessGreen
import com.nust.valentinegarage.core.ui.theme.WarningAmber

enum class StatusType { SUCCESS, WARNING, INFO }

@Composable
fun StatusChip(
    text: String,
    type: StatusType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (type) {
        StatusType.SUCCESS -> SuccessGreen
        StatusType.WARNING -> WarningAmber
        StatusType.INFO -> MaterialTheme.colorScheme.secondary
    }

    Box(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(0.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}
