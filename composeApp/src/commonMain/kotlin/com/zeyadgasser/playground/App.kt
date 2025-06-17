package com.zeyadgasser.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zeyadgasser.playground.badhabits.form.ui.BadHabitFormStateHolder
import com.zeyadgasser.playground.badhabits.list.ui.BadHabitsListStateHolder
import com.zeyadgasser.playground.breath.ui.BreathingCoachAppStateHolder
import com.zeyadgasser.playground.routine.detail.ui.RoutineDetailsStateHolder
import com.zeyadgasser.playground.routine.form.ui.RoutineFormScreenStateHolder
import com.zeyadgasser.playground.routine.list.ui.RoutineListScreenStateHolder
import com.zeyadgasser.playground.sharedui.theme.AppTheme
import com.zeyadgasser.playground.task.detail.ui.TaskDetailScreenStateHolder
import com.zeyadgasser.playground.task.list.ui.TasksScreenStateHolder

@Composable
fun App(modifier: Modifier, onNavHostReady: suspend (NavController) -> Unit = {}) {
    AppTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = BadHabitList) {
            composable<BreathingCoachApp> { BreathingCoachAppStateHolder(modifier) }
            composable<TaskList> { TasksScreenStateHolder(modifier) { navController.navigate(TaskDetail(it)) } }
            composable<TaskDetail> {
                TaskDetailScreenStateHolder(it.toRoute<TaskDetail>().taskId) { navController.popBackStack() }
            }
            composable<RoutineList> {
                RoutineListScreenStateHolder(
                    onRoutineClick = { navController.navigate(RoutineDetail(it)) },
                    onCreateRoutineClick = { navController.navigate(RoutineForm(null)) })
            }
            composable<RoutineDetail> {
                RoutineDetailsStateHolder(
                    routineId = it.toRoute<RoutineDetail>().routineId,
                    onDelete = { navController.popBackStack() },
                    onEdit = { navController.navigate(RoutineForm(it.toRoute<RoutineDetail>().routineId)) })
            }
            composable<RoutineForm> {
                RoutineFormScreenStateHolder(
                    routineId = it.toRoute<RoutineForm>().routineId,
                    onCloseFormClick = { navController.popBackStack() })
            }
            composable<BadHabitList> {
                BadHabitsListStateHolder(
                    onBadHabitClick = { navController.navigate(BadHabitDetail(it)) },
                    onCreateBadHabitClick = { navController.navigate(BadHabitForm(null)) })
            }
//            composable<BadHabitDetail> {
//                BadHabitDetailsStateHolder(
//                    routineId = it.toRoute<BadHabitDetail>().routineId,
//                    onDelete = { navController.popBackStack() },
//                    onEdit = { navController.navigate(BadHabitForm(it.toRoute<BadHabit>().badHabitId)) })
//            }
            composable<BadHabitForm> {
                BadHabitFormStateHolder(
                    badHabitId = it.toRoute<BadHabitForm>().badHabitId,
                    onCloseFormClick = { navController.popBackStack() })
            }
        }
        LaunchedEffect(navController) {
            onNavHostReady(navController)
        }
    }
}
