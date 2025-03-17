package com.zeyadgasser.playground

import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.iosModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    Napier.base(DebugAntilog())
    startKoin { modules(commonModule + iosModule) }
    App(Modifier)
}