package com.zeyadgasser.playground

import android.app.Application
import com.zeyadgasser.playground.breath.di.breathModule
import com.zeyadgasser.playground.database.androidDataBaseModule
import com.zeyadgasser.playground.database.di.dataBaseModule
import com.zeyadgasser.playground.di.androidModule
import com.zeyadgasser.playground.di.commonModule
import com.zeyadgasser.playground.networking.networkingModule
import com.zeyadgasser.playground.routine.data.androidRoutineSharedDataModule
import com.zeyadgasser.playground.routine.data.routineSharedDataModule
import com.zeyadgasser.playground.routine.detail.routineDetailModule
import com.zeyadgasser.playground.routine.form.routineFormModule
import com.zeyadgasser.playground.routine.list.routineListModule
import com.zeyadgasser.playground.routine.sharedpresentation.routineSharedPresentationModule
import com.zeyadgasser.playground.task.data.di.androidTaskSharedDataModule
import com.zeyadgasser.playground.task.data.di.taskSharedDataModule
import com.zeyadgasser.playground.task.detail.di.taskDetailModule
import com.zeyadgasser.playground.task.list.di.taskListModule
import com.zeyadgasser.playground.task.sharedpresentation.di.taskPresentationModule
import com.zeyadgasser.playground.tasks.data.sync.Sync
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin

class PlaygroundApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        startKoin {
            androidContext(this@PlaygroundApplication)
            workManagerFactory()
            modules(
                androidModule + commonModule
                        + networkingModule
                        + dataBaseModule
                        + androidDataBaseModule
                        + taskListModule
                        + taskDetailModule
                        + taskPresentationModule
                        + taskSharedDataModule
                        + androidTaskSharedDataModule
                        + breathModule
                        + androidRoutineSharedDataModule
                        + routineListModule
                        + routineSharedDataModule
                        + routineDetailModule
                        + routineFormModule
                        + routineSharedPresentationModule
            )
        }
        Sync.initialize(this)
    }
}
