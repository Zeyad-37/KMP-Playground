package com.zeyadgasser.playground.di

import ch.protonmail.android.crypto.CryptoLib
import com.zeyadgasser.playground.architecture.di.IS_ANDROID
import com.zeyadgasser.playground.tasks.data.sync.TasksSyncWorker
import com.zeyadgasser.playground.utils.AndroidCryptoHelper
import com.zeyadgasser.playground.utils.CryptoHelper
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    worker { TasksSyncWorker(get(), get(), get()) }
    single(named(IS_ANDROID)) { true }
    single { CryptoLib() }
    single<CryptoHelper> { AndroidCryptoHelper(get()) }
}
