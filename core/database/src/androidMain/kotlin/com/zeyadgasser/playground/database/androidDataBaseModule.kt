package com.zeyadgasser.playground.database

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataBaseModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
}
