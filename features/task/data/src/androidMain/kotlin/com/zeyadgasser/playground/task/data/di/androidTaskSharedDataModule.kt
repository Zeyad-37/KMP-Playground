package com.zeyadgasser.playground.task.data.di

import com.zeyadgasser.playground.task.data.utils.AndroidCryptoHelper
import com.zeyadgasser.playground.task.data.utils.CryptoHelper
import org.koin.dsl.module

val androidTaskSharedDataModule = module {
    single<CryptoHelper> { AndroidCryptoHelper(get()) }
}
