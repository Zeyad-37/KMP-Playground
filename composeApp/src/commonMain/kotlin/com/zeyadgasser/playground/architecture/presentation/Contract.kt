package com.zeyadgasser.playground.architecture.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlin.reflect.KClass

/**
 * An [Input] represents an action that the view model can react to
 */
interface Input

data class CancelInput<I : Input>(val clazz: KClass<I>) : Input

/**
 * A [Result] represents the result of some action taken by the view model in response to an [Input]
 *
 * [Result]'s are reduced by a [Reducer] in order to update the [State]
 */
interface Result

/**
 * An [Effect] is a special type of [Result] that represents a one off action (e.g navigation)
 *
 * [Effect]'s are presented to the view immediately rather than being reduced
 */
interface Effect : Result

/**
 * A [State] represents the state of a screen
 */
interface State : Result

/**
 * Wrapper class to attribute inputs and [Result]s as a pair.
 */
internal data class InputResultFlow(val input: Input, val results: Flow<Result>)

/**
 * A wrapper class to identify async flows vs sync flows
 */
class AsyncResultFlow(val flow: Flow<Result>) : Flow<Result> {
    override suspend fun collect(collector: FlowCollector<Result>) = Unit
}
