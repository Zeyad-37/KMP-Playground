package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.architecture.di.COMPUTATION
import com.zeyadgasser.playground.architecture.di.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val commonModule = module {
    single(named(IO)) { Dispatchers.IO }
    single(named(COMPUTATION)) { Dispatchers.Default }
}