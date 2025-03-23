package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.architecture.di.IS_ANDROID
import org.koin.core.qualifier.named
import org.koin.dsl.module

val iosModule = module {
    single(named(IS_ANDROID)) { false }
}
