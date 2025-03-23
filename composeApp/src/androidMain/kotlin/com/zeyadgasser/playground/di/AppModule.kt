package com.zeyadgasser.playground.di

import ch.protonmail.android.crypto.CryptoLib
import com.zeyadgasser.playground.architecture.di.IS_ANDROID
import com.zeyadgasser.playground.tasks.data.db.AndroidDatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.db.DatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.sync.TasksSyncWorker
import com.zeyadgasser.playground.utils.AndroidCryptoHelper
import com.zeyadgasser.playground.utils.CryptoHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
    worker { TasksSyncWorker(get(), get(), get()) }
    single(named(IS_ANDROID)) { true }
    single { CryptoLib() }
    single<CryptoHelper> { AndroidCryptoHelper(get()) }
}
