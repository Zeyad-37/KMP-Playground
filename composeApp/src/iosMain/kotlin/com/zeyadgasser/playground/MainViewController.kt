package com.zeyadgasser.playground

import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.iosModule
import com.zeyadgasser.playground.tasks.data.di.taskSharedDataModule
import com.zeyadgasser.playground.tasks.presentation.detail.di.taskDetailModule
import com.zeyadgasser.playground.tasks.presentation.list.di.taskListModule
import com.zeyadgasser.playground.tasks.sharedPresentation.di.taskPresentationModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    Napier.base(DebugAntilog())
    startKoin {
        modules(
            commonModule + iosModule
                    + taskListModule
                    + taskDetailModule
                    + taskPresentationModule
                    + taskSharedDataModule
        )
    }
    App(Modifier)
}