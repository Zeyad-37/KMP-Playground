package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.architecture.di.IS_ANDROID
import com.zeyadgasser.playground.utils.CryptoHelper
import com.zeyadgasser.playground.utils.IOSCryptoHelper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val iosModule = module {
    single(named(IS_ANDROID)) { false }
    single<CryptoHelper> { IOSCryptoHelper }
}
