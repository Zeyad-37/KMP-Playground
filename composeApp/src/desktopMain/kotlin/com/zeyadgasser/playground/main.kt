package com.zeyadgasser.playground

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.desktopModule
import com.zeyadgasser.playground.task.sharedpresentation.di.taskPresentationModule
import com.zeyadgasser.playground.tasks.data.di.taskSharedDataModule
import com.zeyadgasser.playground.tasks.presentation.detail.di.taskDetailModule
import com.zeyadgasser.playground.tasks.presentation.list.di.taskListModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun main() = application {
    val state = rememberWindowState(position = WindowPosition(300.dp, 300.dp))
    Window(onCloseRequest = ::exitApplication, title = "KMP Playground", state = state) {
        Napier.base(DebugAntilog())
        startKoin {
            modules(
                commonModule + desktopModule
                        + taskListModule
                        + taskDetailModule
                        + taskPresentationModule
                        + taskSharedDataModule
            )
        }
        App(Modifier)
    }
}