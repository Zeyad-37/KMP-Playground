package com.zeyadgasser.playground

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.zeyadgasser.playground.badhabits.data.badHabitsSharedDataModule
import com.zeyadgasser.playground.badhabits.data.db.di.desktopBadHabitsSharedDataModule
import com.zeyadgasser.playground.badhabits.detail.badHabitsDetailModule
import com.zeyadgasser.playground.badhabits.form.badHabitsFormModule
import com.zeyadgasser.playground.badhabits.list.badHabitsListModule
import com.zeyadgasser.playground.badhabits.sharedpresentation.badHabitsSharedPresentationModule
import com.zeyadgasser.playground.breath.di.breathModule
import com.zeyadgasser.playground.database.desktopDataBaseModule
import com.zeyadgasser.playground.database.di.dataBaseModule
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.desktopModule
import com.zeyadgasser.playground.networking.networkingModule
import com.zeyadgasser.playground.routine.data.di.desktopRoutineSharedDataModule
import com.zeyadgasser.playground.routine.data.routineSharedDataModule
import com.zeyadgasser.playground.routine.detail.routineDetailModule
import com.zeyadgasser.playground.routine.form.routineFormModule
import com.zeyadgasser.playground.routine.list.routineListModule
import com.zeyadgasser.playground.routine.sharedpresentation.routineSharedPresentationModule
import com.zeyadgasser.playground.task.data.di.desktopTaskSharedDataModule
import com.zeyadgasser.playground.task.data.di.taskSharedDataModule
import com.zeyadgasser.playground.task.detail.di.taskDetailModule
import com.zeyadgasser.playground.task.list.di.taskListModule
import com.zeyadgasser.playground.task.sharedpresentation.di.taskPresentationModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun main() = application {
    val state = rememberWindowState(position = WindowPosition(300.dp, 300.dp), size = DpSize.Unspecified)
    Window(onCloseRequest = ::exitApplication, title = "KMP Playground", state = state) {
        Napier.base(DebugAntilog())
        startKoin {
            modules(
                commonModule + desktopModule
                        + networkingModule
                        + dataBaseModule
                        + desktopDataBaseModule
                        + taskListModule
                        + taskDetailModule
                        + taskPresentationModule
                        + taskSharedDataModule
                        + desktopTaskSharedDataModule
                        + breathModule
                        + routineListModule
                        + routineSharedDataModule
                        + routineDetailModule
                        + desktopRoutineSharedDataModule
                        + routineFormModule
                        + routineSharedPresentationModule
                        + badHabitsListModule
                        + badHabitsSharedDataModule
                        + badHabitsFormModule
                        + badHabitsDetailModule
                        + badHabitsSharedPresentationModule
                        + desktopBadHabitsSharedDataModule
            )
        }
        App(Modifier)
    }
}