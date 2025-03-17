package com.zeyadgasser.playground.architecture.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeyadgasser.playground.utils.AtomicBoolean
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlin.reflect.KClass

/**
 * View model that implements the MVI (Model View Intent) architecture pattern
 */
@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
abstract class ViewModel<I : Input, R : Result, S : State, E : Effect>(
    initialState: S,
    private val reducer: Reducer<R, S>? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default, // TODO Create computation dispatcher
) : ViewModel() {

    /**
     * [ViewModel] specific logger
     */
    private val tag: String = this::class.simpleName ?: "ViewModel"

    /**
     * Log uncaught exceptions in [resolve]
     */
    private val logUncaughtExceptions =
        CoroutineExceptionHandler { _, throwable -> Napier.e(throwable.message.orEmpty(), throwable, tag) }

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)

    /**
     * Observe [state] in the view to react to state changes
     */
    val state: StateFlow<S> = _state

    private val _effect: MutableSharedFlow<E> = MutableSharedFlow()

    /**
     * Observe [effect] in the view to react to one time effects
     */
    val effect: Flow<E> = _effect

    private val cancellableInputsMap: MutableMap<KClass<out I>, AtomicBoolean> = mutableMapOf()

    /**
     * Send inputs to be processed by the view model, they will be handled by [resolve] and then the [Result]'s will
     * be passed to the relevant flow
     *
     * @param input the input to be handled
     */
    fun process(input: Input) {
        log("Input", input)
        if (input is CancelInput<*>) {
            cancellableInputsMap[input.clazz as KClass<I>] = AtomicBoolean(true)
        } else {
            flowOf(InputResultFlow(input, resolve(input as I, _state.value)))
                .shareIn(viewModelScope, Lazily)
                .run {
                    // create two streams one for sync and one for async processing
                    val asyncOutcomes = filter { it.results is AsyncResultFlow }
                        .map { it.copy(results = (it.results as AsyncResultFlow).flow) }
                        .flatMapMerge { it.results }
                    val sequentialOutcomes = filter { it.results !is AsyncResultFlow }
                        .flatMapConcat { it.results }
                    merge(asyncOutcomes, sequentialOutcomes)
                        .flowOn(dispatcher + logUncaughtExceptions)
                }.onEach { result ->
                    when (result) {
                        is Effect -> _effect.emit(result as E).also { log("Effect", result) }
                        is State -> _state.update { result as S }.also { log("State", it) }
                        else -> _state.update { state ->
                            log("Result", result)
                            reducer?.reduce(result as R, state)?.also { log("State", it) }
                                ?: throw NullPointerException("Reducer is null")
                        }
                    }
                }.flowOn(dispatcher + logUncaughtExceptions).launchIn(viewModelScope)
        }
    }

    /**
     * Creates a cancellable Flow that can be terminated externally using a specific input type.
     *
     * This function transforms the current Flow into a cancellable one.  The cancellation is triggered
     * by setting a flag associated with the provided `inputClass`. While the flag is set to `true`, the
     * flow continues to emit. When the flag is switched to `false` externally, the flow will cease
     * emitting new values, and it will eventually complete.
     *
     * **Key Features:**
     * - **External Cancellation:** The Flow's emission can be stopped from outside by manipulating a flag.
     * - **Input-Specific Cancellation:** Cancellation is targeted to specific input types, allowing for
     *   multiple independently cancellable Flows.
     * - **Atomic Flag Management:** Uses an `AtomicBoolean` to ensure thread-safe cancellation.
     * - **Clean-up:** Removes the cancellation flag upon flow completion.
     * - **Optional Completion Emission:** Allows for emitting a final result (`toggleOffLoadingResult`)
     *   upon cancellation or normal completion.
     *
     * **How to Cancel:**
     * 1. Obtain the `KClass` representing the input type (e.g., `MyInput::class`).
     * 2. Set the value associated with that `KClass` in the `cancellableInputsMap` to `true`
     * (e.g. `cancellableInputsMap[MyInput::class]?.set(true)` ) to stop the flow.
     *
     * **Important Considerations:**
     * - **Thread Safety:** The cancellation mechanism uses `AtomicBoolean` for safe concurrent access.
     * - **Cancellation is Asynchronous:**  The flow won't stop emitting immediately upon setting the
     *   cancellation flag. It will stop *before* the next emission.
     * - **Flow Completion:** The flow will complete after cancellation, ensuring proper resource release.
     * - **Multiple Flows:** Multiple `makeCancellable` flows with different `inputClass` values can be
     *   active simultaneously, and each one can be cancelled independently.
     * - **Early cancellation:** If `cancellableInputsMap[inputClass]` is set to true before first emission
     *   of the flow, then flow will terminate without emission any value
     *
     * @param inputClass The `KClass` representing the type of input that will be used to trigger
     */
    fun Flow<Result>.makeCancellable(
        inputClass: KClass<out I>,
        toggleOffLoadingResult: Result? = null,
    ): Flow<Result> =
        onStart { cancellableInputsMap[inputClass] = AtomicBoolean(false) }
            .takeWhile { cancellableInputsMap[inputClass]?.get() == false }
            .onCompletion {
                toggleOffLoadingResult?.let { it1 -> emit(it1) }
                cancellableInputsMap.remove(inputClass)
            }

    fun Flow<Result>.executeInParallel(): AsyncResultFlow = AsyncResultFlow(this)

    /**
     * Map [input]'s to [Result]'s
     *
     * @param state the current [State]
     * @param input the [Input] to be handled
     *
     * @return a [Flow] of [Result] representing the result of handling the input
     */
    protected abstract fun resolve(input: I, state: S): Flow<Result>

    /**
     * Log [Input]'s and the resulting [Result]'s, [Effect]'s and [State]
     */
    private fun log(type: String, item: Any) = Napier.d("$type : $item", tag = tag)
}
