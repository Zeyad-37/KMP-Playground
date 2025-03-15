package com.zeyadgasser.playground

import android.app.Application
import com.zeyadgasser.playground.di.appModule
import com.zeyadgasser.playground.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaygroundApplication)
            modules(appModule + commonModule)
        }
    }
}
