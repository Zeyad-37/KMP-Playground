package com.zeyadgasser.playground.di

import com.zeyadgasser.playground.networking.KtorHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO = "io"
const val COMPUTATION = "default"
val commonModule = module {
    single(named(IO)) { Dispatchers.IO }
    single(named(COMPUTATION)) { Dispatchers.Default }
    single { KtorHttpClient.json() }
    single { KtorHttpClient.httpClient(get()) }
}