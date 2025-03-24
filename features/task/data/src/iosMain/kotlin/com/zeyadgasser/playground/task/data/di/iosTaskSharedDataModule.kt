package com.zeyadgasser.playground.task.data.di

import com.zeyadgasser.playground.task.data.utils.CryptoHelper
import com.zeyadgasser.playground.task.data.utils.IOSCryptoHelper
import org.koin.dsl.module

val iosTaskSharedDataModule = module {
    single<CryptoHelper> { IOSCryptoHelper }
}
