package com.zeyadgasser.playground.task.data.di

import com.zeyadgasser.playground.task.data.db.PlaygroundDatabase
import com.zeyadgasser.playground.task.data.db.getPlaygroundDatabase
import com.zeyadgasser.playground.task.data.utils.CryptoHelper
import com.zeyadgasser.playground.task.data.utils.DesktopCryptoHelper
import org.koin.dsl.module

val desktopTaskSharedDataModule = module {
    single<CryptoHelper> { DesktopCryptoHelper }
    single<PlaygroundDatabase> { getPlaygroundDatabase() }
}
