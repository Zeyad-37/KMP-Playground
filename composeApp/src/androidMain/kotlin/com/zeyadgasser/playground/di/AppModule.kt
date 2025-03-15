package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.tasks.data.db.AndroidDatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.db.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
}
