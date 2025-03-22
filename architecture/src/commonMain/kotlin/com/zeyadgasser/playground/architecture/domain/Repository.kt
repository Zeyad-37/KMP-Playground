package com.zeyadgasser.playground.architecture.domain

import kotlinx.coroutines.CoroutineDispatcher

interface Repository {

    val ioDispatcher: CoroutineDispatcher
}
