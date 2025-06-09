package com.zeyadgasser.playground.architecture.presentation

import kotlinx.coroutines.flow.Flow

interface InputHandler<I : Input, S : State> {
    suspend operator fun invoke(input: I, state: S): Flow<Result>
}
