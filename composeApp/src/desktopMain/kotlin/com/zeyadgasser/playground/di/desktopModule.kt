package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.architecture.di.IS_ANDROID
import com.zeyadgasser.playground.database.DatabaseDriverFactory
import com.zeyadgasser.playground.database.DesktopDatabaseDriverFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val desktopModule = module {
    single<DatabaseDriverFactory> { DesktopDatabaseDriverFactory() }
    single(named(IS_ANDROID)) { false }
}
