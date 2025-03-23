package com.zeyadgasser.playground.di

import ch.protonmail.android.crypto.CryptoLib
import com.zeyadgasser.playground.architecture.di.IS_ANDROID
import com.zeyadgasser.playground.tasks.data.sync.TasksSyncWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module

val androidModule = module {
    worker { TasksSyncWorker(get(), get(), get()) }
    single(named(IS_ANDROID)) { true }
    single { CryptoLib() }
}
