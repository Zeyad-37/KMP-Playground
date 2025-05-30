package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.routine.list.viewmodel.CreateRoutineInput
import com.zeyadgasser.playground.routine.list.viewmodel.GoToCreateRoutineEffect
import com.zeyadgasser.playground.routine.list.viewmodel.GoToRoutineDetailsEffect
import com.zeyadgasser.playground.routine.list.viewmodel.HideDialogEffect
import com.zeyadgasser.playground.routine.list.viewmodel.HideDialogInput
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.EmptyState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.ErrorState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.InitialState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.SuccessState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListViewModel
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineRatedInput
import com.zeyadgasser.playground.routine.list.viewmodel.ShowRoutineRatingDialogEffect
import com.zeyadgasser.playground.routine.resources.Res
import com.zeyadgasser.playground.routine.resources.add_icon
import com.zeyadgasser.playground.routine.resources.add_routine
import com.zeyadgasser.playground.routine.resources.routines
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM.Companion.EMPTY_ROUTINE
import com.zeyadgasser.playground.sharedui.composables.ErrorScreen
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun RoutineListScreenStateHolder(
    viewModel: RoutineListViewModel = koinInject(),
    onRoutineClick: (Long) -> Unit,
    onCreateRoutineClick: () -> Unit,
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val routinesState by viewModel.state.collectAsState()
    var showDialogPair by remember { mutableStateOf(false to EMPTY_ROUTINE) }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is GoToRoutineDetailsEffect -> onRoutineClick(it.routineId)
                GoToCreateRoutineEffect -> onCreateRoutineClick()
                is ShowRoutineRatingDialogEffect -> showDialogPair = true to it.routine
                HideDialogEffect -> showDialogPair = false to EMPTY_ROUTINE
            }
        }
    }
    RoutineListScreenContent(Modifier, routinesState, showDialogPair, snackBarHostState) {
        viewModel.process(
            it
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoutineListScreenContent(
    modifier: Modifier,
    state: RoutineListState,
    showRatingDialog: Pair<Boolean, RoutinePM>,
    snackBarHostState: SnackbarHostState,
    listState: LazyListState = rememberLazyListState(),
    process: (Input) -> Unit,
) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.routines),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                actions = {
                    IconButton({ process(CreateRoutineInput) }) {
                        Icon(painterResource(Res.drawable.add_icon), stringResource(Res.string.add_routine))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { Snackbar(it, contentColor = Color.Red) })
        },
    ) { innerPadding ->
        if (showRatingDialog.first)
        // TODO show rating
            AlertDialog(
                onDismissRequest = { process(HideDialogInput) },
                confirmButton = {
                    TextButton({
                        process(
                            RoutineRatedInput(
                                showRatingDialog.second,
                                rating = 1
                            )
                        )
                    }) { Text("Confirm") }
                },
                dismissButton = { TextButton({ process(HideDialogInput) }) { Text("Dismiss") } },
                title = { Text("Rating") },
                text = { Text("Dialog effect!") },
            )

        when (state) {
            is InitialState -> process(LoadRoutineListInput)
            is ErrorState -> ErrorScreen(state.message)
            EmptyState -> EmptyRoutineScreen(Modifier.padding(innerPadding)) { process(it) }
            is SuccessState ->
                RoutineList(Modifier.padding(innerPadding), state.routine, listState) { process(it) }
        }
    }
}
