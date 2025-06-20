package com.zeyadgasser.playground.task.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.sharedui.composables.ErrorScreen
import com.zeyadgasser.playground.sharedui.composables.LoadingView
import com.zeyadgasser.playground.task.list.viewmodel.CantCheckTaskEffect
import com.zeyadgasser.playground.task.list.viewmodel.GoToTaskDetailsEffect
import com.zeyadgasser.playground.task.list.viewmodel.HideDialogEffect
import com.zeyadgasser.playground.task.list.viewmodel.HideDialogInput
import com.zeyadgasser.playground.task.list.viewmodel.LoadTasksInput
import com.zeyadgasser.playground.task.list.viewmodel.ShowDialogEffect
import com.zeyadgasser.playground.task.list.viewmodel.ShowDialogInput
import com.zeyadgasser.playground.task.list.viewmodel.TasksState
import com.zeyadgasser.playground.task.list.viewmodel.TasksViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun TasksScreenStateHolder(
    modifier: Modifier, viewModel: TasksViewModel = koinInject(), onTaskClick: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val tasksState by viewModel.state.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is GoToTaskDetailsEffect -> onTaskClick(it.taskId)
                is CantCheckTaskEffect -> coroutineScope.launch {
                    snackBarHostState.showSnackbar(
                        "",
//                        stringResource(Res.string.cant_check_a_task),
                        duration = SnackbarDuration.Short
                    )
                }

                ShowDialogEffect -> showDialog = true
                HideDialogEffect -> showDialog = false
            }
        }
    }
    TasksScreenContent(modifier, tasksState, showDialog, snackBarHostState) { viewModel.process(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreenContent(
    modifier: Modifier,
    state: TasksState,
    showDialog: Boolean,
    snackBarHostState: SnackbarHostState,
    process: (Input) -> Unit,
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var allTabLabel by remember { mutableStateOf("All tasks") }
    var upcomingTabLabel by remember { mutableStateOf("Upcoming Tasks") }
    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                {
                    Text( //fixme(java.lang.IllegalStateException:  No instrumentation registered! Must run under a registering instrumentation.)
//                            text = stringResource(Res.string.app_name),
                        text = "KMP Playground",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { process(if (showDialog) HideDialogInput else ShowDialogInput) },
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineSmall
                    )
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
        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                text = { Text(allTabLabel, color = MaterialTheme.colorScheme.onBackground) },
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )
            Tab(
                text = { Text(upcomingTabLabel, color = MaterialTheme.colorScheme.onBackground) },
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }
            )
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { process(HideDialogInput) },
                confirmButton = { TextButton({ process(HideDialogInput) }) { Text("Confirm") } },
                dismissButton = { TextButton({ process(HideDialogInput) }) { Text("Dismiss") } },
                title = { Text("Dialog") },
                text = { Text("Dialog effect!") },
            )
        }
        if (state.isLoading) {
            LoadingView(modifier.padding(innerPadding))
        }
        when (state) {
            is TasksState.InitialState -> process(LoadTasksInput)
            is TasksState.ErrorState -> ErrorScreen(state.message)
            is TasksState.SuccessState -> {
//                allTabLabel = stringResource(Res.string.all_tasks_tab_label, state.allTasks.size)
                allTabLabel = "All tasks (${state.allTasks.size})"
                upcomingTabLabel = "Upcoming tasks (${state.upcomingTasks.size})"
//                upcomingTabLabel = stringResource(Res.string.upcoming_tasks_tab_label, state.upcomingTasks.size)
                when (selectedTabIndex) {
                    0 -> TaskList(state.allTasks) { process(it) }
                    1 -> TaskList(state.upcomingTasks) { process(it) }
                }
            }
        }
    }
}
