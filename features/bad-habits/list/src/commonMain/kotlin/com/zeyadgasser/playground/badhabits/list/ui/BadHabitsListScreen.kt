package com.zeyadgasser.playground.badhabits.list.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState.EmptyState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState.ErrorState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState.InitialState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListState.SuccessState
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListViewModel
import com.zeyadgasser.playground.badhabits.list.viewmodel.ErrorEffect
import com.zeyadgasser.playground.badhabits.list.viewmodel.GoToBadHabitDetailsEffect
import com.zeyadgasser.playground.badhabits.list.viewmodel.GoToCreateBadHabitEffect
import com.zeyadgasser.playground.badhabits.list.viewmodel.LoadBadHabitListInput
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInput.CreateBadHabitInput
import com.zeyadgasser.playground.sharedui.composables.ErrorScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun BadHabitsListStateHolder(
    viewModel: BadHabitListViewModel = koinInject(),
    onBadHabitClick: (Long) -> Unit,
    onCreateBadHabitClick: () -> Unit,
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is GoToBadHabitDetailsEffect -> onBadHabitClick(it.badHabitId)
                GoToCreateBadHabitEffect -> onCreateBadHabitClick()
                is ErrorEffect -> snackBarHostState.showSnackbar(it.message)
            }
        }
    }
    BadHabitListScreenContent(Modifier, state, snackBarHostState) { viewModel.process(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadHabitListScreenContent(
    modifier: Modifier,
    state: BadHabitListState,
    snackBarHostState: SnackbarHostState,
    listState: LazyListState = rememberLazyListState(),
    process: (Input) -> Unit,
) {
    Scaffold(
        modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bad Habits",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { process(CreateBadHabitInput) },
                icon = { Icon(Icons.Filled.Add, "Add new item") }, // The icon part
                text = { Text("Add Item") }, // The text part
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState, snackbar = { Snackbar(it, contentColor = Color.Red) })
        },
        containerColor = Color.White,
    ) { innerPadding ->
        when (state) {
            is InitialState -> process(LoadBadHabitListInput)
            is ErrorState -> ErrorScreen(state.message)
            EmptyState -> NoBadHabitsTrackedScreen { process(CreateBadHabitInput) }
            is SuccessState ->
                BadHabitList(modifier.padding(innerPadding), state.date, state.badHabits, listState)
                { process(it) }
        }
    }
}
