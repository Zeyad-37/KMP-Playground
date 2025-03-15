@file:OptIn(ExperimentalContracts::class)

package com.zeyadgasser.playground.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

expect class AtomicInt(initialValue: Int) {

    fun get(): Int
    fun set(newValue: Int)
    fun getAndSet(value: Int): Int

    fun incrementAndGet(): Int
    fun decrementAndGet(): Int

    fun addAndGet(delta: Int): Int

    fun compareAndSet(expected: Int, new: Int): Boolean
}

var AtomicInt.value: Int
    get() = get()
    set(value) {
        set(value)
    }

/**
 * Infinite loop that reads this atomic variable and performs the specified [action] on its value.
 */
inline fun AtomicInt.loop(action: (Int) -> Unit): Nothing {
    contract { callsInPlace(action, InvocationKind.AT_LEAST_ONCE) }
    do {
        action(value)
    } while (true)
}

inline fun AtomicInt.tryUpdate(function: (Int) -> Int): Boolean {
    contract { callsInPlace(function, InvocationKind.EXACTLY_ONCE) }
    return tryUpdate(function) { _, _ -> }
}

inline fun AtomicInt.update(function: (Int) -> Int) {
    contract { callsInPlace(function, InvocationKind.AT_LEAST_ONCE) }
    update(function) { _, _ -> }
}

/**
 * Updates variable atomically using the specified [function] of its value and returns its old value.
 */
inline fun AtomicInt.getAndUpdate(function: (Int) -> Int): Int {
    contract { callsInPlace(function, InvocationKind.AT_LEAST_ONCE) }
    return update(function) { old, _ -> old }
}

/**
 * Updates variable atomically using the specified [function] of its value and returns its new value.
 */
inline fun AtomicInt.updateAndGet(function: (Int) -> Int): Int {
    contract { callsInPlace(function, InvocationKind.AT_LEAST_ONCE) }
    return update(function) { _, new -> new }
}

@PublishedApi
internal inline fun <R> AtomicInt.update(function: (Int) -> Int, transform: (old: Int, new: Int) -> R): R {
    contract {
        callsInPlace(function, InvocationKind.AT_LEAST_ONCE)
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    loop { cur ->
        val upd = function(value)
        if (compareAndSet(cur, upd)) return transform(cur, upd)
    }
}

@PublishedApi
internal inline fun AtomicInt.tryUpdate(
    function: (Int) -> Int,
    onUpdated: (old: Int, new: Int) -> Unit,
): Boolean {
    contract {
        callsInPlace(function, InvocationKind.EXACTLY_ONCE)
        callsInPlace(onUpdated, InvocationKind.AT_MOST_ONCE)
    }
    val cur = value
    val upd = function(cur)
    return compareAndSet(cur, upd).also { if (it) onUpdated(cur, upd) }
}