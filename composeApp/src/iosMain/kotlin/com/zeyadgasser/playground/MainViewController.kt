package com.zeyadgasser.playground

import androidx.compose.ui.window.ComposeUIViewController
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.iosModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    startKoin { modules(commonModule + iosModule) }
    App()
}