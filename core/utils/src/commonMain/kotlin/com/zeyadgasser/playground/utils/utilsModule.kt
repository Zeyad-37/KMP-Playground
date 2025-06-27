package com.zeyadgasser.playground.utils

import org.koin.dsl.module

var utilModule = module {
    single { TimeService }
}
