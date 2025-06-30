package com.zeyadgasser.playground

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zeyadgasser.playground.BottomNavItem.BadHabitList
import com.zeyadgasser.playground.BottomNavItem.Profile
import com.zeyadgasser.playground.BottomNavItem.RoutineList
import com.zeyadgasser.playground.badhabits.detail.ui.BadHabitDetailsStateHolder
import com.zeyadgasser.playground.badhabits.form.ui.BadHabitFormStateHolder
import com.zeyadgasser.playground.badhabits.list.ui.BadHabitsListStateHolder
import com.zeyadgasser.playground.breath.ui.BreathingCoachAppStateHolder
import com.zeyadgasser.playground.profile.ui.ProfileScreen
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
        LaunchedEffect(navController) { onNavHostReady(navController) }
        val bottomNavItems = listOf(RoutineList, BadHabitList, Profile)
        Scaffold(
            bottomBar = { // fixme Not showing on iOS
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    val navBackStackEntry = navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry.value?.destination?.route
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            icon = { Icon(imageVector = item.icon, contentDescription = null) },
                            label = { Text(item.title) },
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(navController = navController, startDestination = BadHabitList.route) {
                    composable<BreathingCoachApp> { BreathingCoachAppStateHolder(modifier) }
                    composable<TaskList> {
                        TasksScreenStateHolder(modifier) { navController.navigate(TaskDetail(it)) }
                    }
                    composable<TaskDetail> {
                        TaskDetailScreenStateHolder(it.toRoute<TaskDetail>().taskId) { navController.popBackStack() }
                    }
                    composable(RoutineList.route) {
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
                    composable(BadHabitList.route) {
                        BadHabitsListStateHolder(
                            onBadHabitClick = { navController.navigate(BadHabitDetail(it)) },
                            onCreateBadHabitClick = { navController.navigate(BadHabitForm(null)) })
                    }
                    composable<BadHabitDetail> {
                        BadHabitDetailsStateHolder(
                            badHabitId = it.toRoute<BadHabitDetail>().badHabitId,
                            onDelete = { navController.popBackStack() },
                            onEdit = { navController.navigate(BadHabitForm(it.toRoute<BadHabitDetail>().badHabitId)) })
                    }
                    composable<BadHabitForm> {
                        BadHabitFormStateHolder(
                            badHabitId = it.toRoute<BadHabitForm>().badHabitId,
                            onCloseFormClick = { navController.popBackStack() })
                    }
                    composable(Profile.route) { ProfileScreen() }
                }
            }
        }
    }
}
