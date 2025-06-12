package com.zeyadgasser.playground

import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.zeyadgasser.playground.breath.di.breathModule
import com.zeyadgasser.playground.database.di.dataBaseModule
import com.zeyadgasser.playground.database.iosDataBaseModule
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.di.iosModule
import com.zeyadgasser.playground.networking.networkingModule
import com.zeyadgasser.playground.routine.data.di.iosRoutineSharedDataModule
import com.zeyadgasser.playground.routine.data.routineSharedDataModule
import com.zeyadgasser.playground.routine.detail.routineDetailModule
import com.zeyadgasser.playground.routine.form.routineFormModule
import com.zeyadgasser.playground.routine.list.routineListModule
import com.zeyadgasser.playground.routine.sharedpresentation.routineSharedPresentationModule
import com.zeyadgasser.playground.task.data.di.iosTaskSharedDataModule
import com.zeyadgasser.playground.task.list.di.taskListModule
import com.zeyadgasser.playground.task.sharedpresentation.di.taskPresentationModule
import com.zeyadgasser.playground.task.data.di.taskSharedDataModule
import com.zeyadgasser.playground.task.detail.di.taskDetailModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    Napier.base(DebugAntilog())
    startKoin {
        modules(
            commonModule + iosModule
                    + networkingModule
                    + dataBaseModule
                    + iosDataBaseModule
                    + taskListModule
                    + taskDetailModule
                    + taskPresentationModule
                    + taskSharedDataModule
                    + iosTaskSharedDataModule
                    + breathModule
                    + routineListModule
                    + routineSharedDataModule
                    + iosRoutineSharedDataModule
                    + routineDetailModule
                    + routineFormModule
                    + routineSharedPresentationModule
        )
    }
    App(Modifier)
}