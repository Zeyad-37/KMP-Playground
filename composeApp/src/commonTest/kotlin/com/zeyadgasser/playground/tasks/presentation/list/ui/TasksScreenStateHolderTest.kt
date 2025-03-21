package com.zeyadgasser.playground.tasks.presentation.list.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.zeyadgasser.playground.tasks.presentation.list.viewmodel.TasksState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TasksScreenStateHolderTest {

    @Test
    fun launchTheApp_assertHomeScreenIsDisplayed() = runComposeUiTest {
        setContent {
            val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
            ListScreen(Modifier).TasksScreenContent(
                Modifier,
                TasksState.InitialState(true),
                false,
                snackBarHostState
            ) {}
        }
    }
}