package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val maxStars = 5
    Row(
        modifier = modifier.selectableGroup().fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val starIcon = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = starIcon,
                contentDescription = null, // No accessibility content for decorative icons
                modifier = Modifier
                    .size(52.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat()) // Update rating to the clicked star
                        }
                    ),
                tint = if (isSelected) Color.Yellow else Color.Gray
            )
        }
    }
}