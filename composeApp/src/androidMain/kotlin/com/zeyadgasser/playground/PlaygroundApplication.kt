package com.zeyadgasser.playground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaygroundApplication)
//            modules(appModule + commonModule)
        }
    }
}
