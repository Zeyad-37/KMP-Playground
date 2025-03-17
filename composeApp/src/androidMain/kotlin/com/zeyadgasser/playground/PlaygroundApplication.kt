package com.zeyadgasser.playground

import android.app.Application
import com.zeyadgasser.playground.di.appModule
import com.zeyadgasser.playground.di.commonModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        startKoin {
            androidContext(this@PlaygroundApplication)
            modules(appModule + commonModule)
        }
    }
}
