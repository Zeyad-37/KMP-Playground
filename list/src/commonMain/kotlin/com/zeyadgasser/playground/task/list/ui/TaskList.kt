package com.zeyadgasser.playground.task.list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM
import com.zeyadgasser.playground.sharedui.theme.AppTheme
import com.zeyadgasser.playground.task.list.viewmodel.TaskCheckedInput
import com.zeyadgasser.playground.task.list.viewmodel.TaskClickedInput
import com.zeyadgasser.playground.task.list.viewmodel.TasksInput
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskList(
    list: List<TaskPM>,
    listState: LazyListState = rememberLazyListState(),
    process: (TasksInput) -> Unit,
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = 52.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),
        listState
    ) {
        items(list, { item -> item.id }) { task ->
            TaskListItem(
                image = task.image,
                title = task.encryptedTitle,
                description = task.encryptedDescription,
                createdDate = task.creationDate,
                dueDate = task.dueDate,
                done = task.done,
                onItemClick = { process(TaskClickedInput(task.id)) },
                onCheckedChange = { process(TaskCheckedInput(task)) }
            )
        }
    }
}

@Preview
@Composable
fun TaskListPreview() {
    AppTheme {
        TaskList(
            listOf(
                TaskPM(
                    creationDate = "2024-12-17 • 12:00",
                    dueDate = "2024-12-17 • 13:00",
                    encryptedDescription = "Encrypted description",
                    encryptedTitle = "Encrypted Title",
                    id = "1",
                    image = "",
                    done = false,
                    dependencies = listOf("2", "3")
                ),
                TaskPM(
                    creationDate = "2024-12-17 • 14:00",
                    dueDate = "2024-12-17 • 15:00",
                    encryptedDescription = "Encrypted description",
                    encryptedTitle = "Encrypted Title",
                    id = "2",
                    image = "",
                    done = true,
                    dependencies = listOf("3")
                )
            ),
            rememberLazyListState(),
            {}
        )
    }
}
