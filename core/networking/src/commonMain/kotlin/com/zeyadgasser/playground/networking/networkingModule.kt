package com.zeyadgasser.playground.networking

import org.koin.dsl.module

val networkingModule = module {
    single { KtorHttpsClient.json() }
    single { KtorHttpsClient.httpClient(get()) }
}