package com.zeyadgasser.playground

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.desktopModule
import org.koin.core.context.startKoin

fun main() = application {
    val state = rememberWindowState(position = WindowPosition(300.dp, 300.dp))
    Window(onCloseRequest = ::exitApplication, title = "KMP Playground", state = state) {
        startKoin { modules(commonModule + desktopModule) }
        App()
    }
}