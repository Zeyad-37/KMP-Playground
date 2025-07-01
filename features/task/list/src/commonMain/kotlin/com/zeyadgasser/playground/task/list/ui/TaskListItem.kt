package com.zeyadgasser.playground.task.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zeyadgasser.playground.task.sharedpresentation.ui.TaskItemTextSlot
import com.zeyadgasser.playground.sharedui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskListItem(
    image: String?,
    title: String,
    description: String,
    createdDate: String,
    dueDate: String,
    done: Boolean,
    onItemClick: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Row(modifier = Modifier.clickable { onItemClick() }) {
        Column {
            Card(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    modifier = Modifier.size(48.dp),
                    model = image,
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Status"
            )
            Checkbox(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                checked = done,
                onCheckedChange = onCheckedChange
            )
        }
        TaskItemTextSlot(title, description, createdDate, dueDate)
    }
}

@Preview
@Composable
fun TaskListItemPreview() {
    AppTheme {
        TaskListItem(
            image = null,
            title = "Title",
            description = "Description",
            createdDate = "2024-12-17 • 12:00",
            dueDate = "2024-12-18 • 12:00",
            done = false,
        )
    }
}
