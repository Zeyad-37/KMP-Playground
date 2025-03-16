package com.zeyadgasser.playground.tasks.presentation.detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zeyadgasser.playground.sharedUI.theme.AppTheme
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskItemTextSlot
import com.zeyadgasser.playground.tasks.sharedPresentation.TaskPM
import kmpplayground.composeapp.generated.resources.Res
import kmpplayground.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskDetail(modifier: Modifier = Modifier, task: TaskPM) {
    var showButton by remember { mutableStateOf(false) }
    Column(modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = task.image,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            clipToBounds = true,
            onError = { showButton = true },
            onSuccess = { showButton = false },
            error = painterResource(Res.drawable.compose_multiplatform),
            placeholder = painterResource(Res.drawable.compose_multiplatform),
        )
        TaskItemTextSlot(task.encryptedTitle, task.encryptedDescription, task.creationDate, task.dueDate)
        if (showButton) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
//                    colors = ButtonColors(Purple, White, TaupeGray, OffWhite),
                    shape = AbsoluteCutCornerShape(2.dp)
                ) {
                    Text("Download image")
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskDetailPreview(modifier: Modifier = Modifier) {
    AppTheme {
        TaskDetail(
            task = TaskPM(
                "creationDate",
                "dueDate",
                "description",
                "title",
                "id",
                "image",
                false,
                emptyList()
            )
        )
    }
}
