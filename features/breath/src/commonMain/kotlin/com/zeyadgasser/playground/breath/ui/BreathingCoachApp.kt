package com.zeyadgasser.playground.breath.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.ExerciseDetail
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.ExerciseList
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.Loading
import com.zeyadgasser.playground.breath.viewmodel.BreathingViewModel
import org.koin.compose.koinInject

@Composable
fun BreathingCoachAppStateHolder(modifier: Modifier, viewModel: BreathingViewModel = koinInject()) {
//        val navigator = LocalNavigator.currentOrThrow
//        val coroutineScope = rememberCoroutineScope()
//        val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val viewState by viewModel.uiState.collectAsStateWithLifecycle()
//        var showDialog by remember { mutableStateOf(false) }
//        LaunchedEffect(Unit) {
//            viewModel.effect.collectLatest {
//                when (it) {
//                    is GoToTaskDetailsEffect -> navigator.push(DetailScreen(it.taskId, modifier))
//                    is CantCheckTaskEffect -> coroutineScope.launch {
//                        snackBarHostState.showSnackbar(
//                            "",
////                            stringResource(Res.string.cant_check_a_task),
//                            duration = SnackbarDuration.Short
//                        )
//                    }
//
//                    ShowDialogEffect -> showDialog = true
//                    HideDialogEffect -> showDialog = false
//                }
//            }
    BreathingCoachApp(
        viewState = viewState,
        onExerciseSelected = { id -> viewModel.selectExercise(id) },
        onPlayPauseClicked = {
            // Decide whether to play or pause based on current state
            val currentScreen: BreathingCoachScreenState = viewState
            if (currentScreen is ExerciseDetail) {
                if (currentScreen.isPlaying) {
                    viewModel.pauseExercise()
                } else {
                    viewModel.startExercise()
                }
            }
        },
        onStopClicked = { viewModel.stopExercise() },
        onBackClicked = { viewModel.goBackToList() }
    )
}

@Composable
fun BreathingCoachApp(
    viewState: BreathingCoachScreenState,
    onExerciseSelected: (String) -> Unit,
    onPlayPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) { // Use Box or other layout as needed
        when (viewState) {
            is Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is ExerciseList -> ExerciseListScreen(viewState.exercises, onExerciseSelected)
            is ExerciseDetail -> ExerciseDetailScreen(
                viewState, onPlayPauseClicked, onStopClicked, onBackClicked, modifier,
            )
        }
    }
}
