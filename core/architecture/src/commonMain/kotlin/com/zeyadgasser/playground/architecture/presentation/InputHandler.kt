package com.zeyadgasser.playground.architecture.presentation

import kotlinx.coroutines.flow.Flow

typealias InputHandlerType = InputHandler<out Input, out State>

interface InputHandler<I : Input, S : State> {

    suspend operator fun invoke(input: I, state: S): Flow<Result>
}
