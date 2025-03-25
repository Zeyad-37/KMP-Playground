package com.zeyadgasser.playground.database

import org.koin.dsl.module

val jsDataBaseModule = module {
    single<DatabaseDriverFactory> { JSDatabaseDriverFactory() }
}
