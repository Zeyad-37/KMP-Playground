package com.zeyadgasser.playground.task.detail.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.sharedui.composables.ErrorScreen
import com.zeyadgasser.playground.sharedui.composables.LoadingView
import com.zeyadgasser.playground.task.detail.viewmodel.BackButtonTappedInput
import com.zeyadgasser.playground.task.detail.viewmodel.GoBackEffect
import com.zeyadgasser.playground.task.detail.viewmodel.LoadTaskInput
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailInput
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState.ErrorState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState.InitialState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailState.SuccessState
import com.zeyadgasser.playground.task.detail.viewmodel.TaskDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun TaskDetailScreenStateHolder(
    taskId: String,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = koinInject(),
    onBackPressed: () -> Unit,
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val taskDetailState by viewModel.state.collectAsStateWithLifecycle(InitialState(false, taskId))
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                GoBackEffect -> onBackPressed()
            }
        }
    }
    TaskDetailScreenContent(modifier, taskDetailState, snackBarHostState) { viewModel.process(it) }
}

@Composable
fun TaskDetailScreenContent(
    modifier: Modifier = Modifier,
    state: TaskDetailState,
    snackBarHostState: SnackbarHostState,
    process: (TaskDetailInput) -> Unit,
) {
    var taskId by remember { mutableStateOf("") }
    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                {
                    Text(
                        text = "KMP Playground",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h3
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { process(BackButtonTappedInput) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { Snackbar(it, contentColor = Color.Red) }
            )
        },
    ) { innerPadding ->
        if (state.isLoading) {
            LoadingView(modifier.padding(innerPadding))
        }
        when (state) {
            is InitialState -> process(LoadTaskInput(state.taskId)).also { taskId = state.taskId }
            is ErrorState -> ErrorScreen(state.message)
            is SuccessState -> TaskDetail(Modifier, state.task)
        }
    }
}
