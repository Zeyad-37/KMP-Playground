package com.zeyadgasser.playground.database.di

import com.zeyadgasser.playground.database.SharedDataBase
import org.koin.dsl.module

val dataBaseModule = module {
    single { SharedDataBase(get()) }
}
