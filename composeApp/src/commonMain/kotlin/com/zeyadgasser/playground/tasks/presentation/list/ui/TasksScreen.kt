package com.zeyadgasser.playground.tasks.presentation.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.sharedUI.composables.ErrorScreen
import com.zeyadgasser.playground.tasks.presentation.detail.ui.DetailScreen
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.CantCheckTaskEffect
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.GoToTaskDetailsEffect
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.HideDialogEffect
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.HideDialogInput
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.LoadTasksInput
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.ShowDialogEffect
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.ShowDialogInput
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksViewModel
import kmpplayground.composeapp.generated.resources.Res
import kmpplayground.composeapp.generated.resources.all_tasks_tab_label
import kmpplayground.composeapp.generated.resources.app_name
import kmpplayground.composeapp.generated.resources.upcoming_tasks_tab_label
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

data class ListScreen(val modifier: Modifier) : Screen {
    @Composable
    override fun Content() {
        TasksScreenStateHolder(modifier)
    }

    @Composable
    fun TasksScreenStateHolder(modifier: Modifier, viewModel: TasksViewModel = koinInject()) {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
        val tasksState by viewModel.state.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest {
                when (it) {
                    is GoToTaskDetailsEffect -> navigator.push(DetailScreen(it.taskId, modifier))
                    is CantCheckTaskEffect -> coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            "Cant check a task as done that still has dependencies", // fixme
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

    @Composable
    fun TasksScreenContent(
        modifier: Modifier,
        state: TasksState,
        showDialog: Boolean,
        snackBarHostState: SnackbarHostState,
        process: (Input) -> Unit,
    ) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        var allTabLabel by remember { mutableStateOf("All tasks") }
        var upcomingTabLabel by remember { mutableStateOf("Upcoming Tasks") }
        Scaffold(
            modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    {
                        Text(
                            text = stringResource(Res.string.app_name),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { process(if (showDialog) HideDialogInput else ShowDialogInput) },
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.h3
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
//        PullToRefreshBox(
//            state.isLoading,
//            { process(LoadTasksInput) },
//            Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    text = { Text(allTabLabel, color = MaterialTheme.colors.onBackground) },
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 }
                )
                Tab(
                    text = { Text(upcomingTabLabel, color = MaterialTheme.colors.onBackground) },
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
            when (state) {
                is TasksState.InitialState -> process(LoadTasksInput)
                is TasksState.ErrorState -> ErrorScreen(state.message)
                is TasksState.SuccessState -> {
                    allTabLabel = stringResource(Res.string.all_tasks_tab_label, state.allTasks.size)
                    upcomingTabLabel =
                        stringResource(Res.string.upcoming_tasks_tab_label, state.upcomingTasks.size)
                    when (selectedTabIndex) {
                        0 -> TaskList(state.allTasks) { process(it) }
                        1 -> TaskList(state.upcomingTasks) { process(it) }
                    }
                }
            }
//        }
        }
    }
}