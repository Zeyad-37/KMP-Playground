package com.zeyadgasser.playground.tasks.sharedPresentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.DateRange
import androidx.compose.material.icons.sharp.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TaskItemTextSlot(title: String, description: String, createdDate: String, dueDate: String) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = title)
        Text(text = description, style = MaterialTheme.typography.body2)
        Row {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Sharp.DateRange),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                    Text(
                        text = "Created:",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = createdDate,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.body2,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Sharp.Notifications),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                    Text(
                        text = "Due:",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = dueDate,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
        }
    }
}
