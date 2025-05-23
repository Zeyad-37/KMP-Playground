@file:OptIn(ExperimentalContracts::class)
package com.zeyadgasser.playground.architecture.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class AtomicBoolean(value: Boolean) {
    private val inner = AtomicInt(value.toInt())

    var value: Boolean
        get() = inner.value != 0
        set(value) {
            inner.value = value.toInt()
        }

    fun compareAndSet(expected: Boolean, new: Boolean): Boolean =
        inner.compareAndSet(expected.toInt(), new.toInt())

    fun get(): Boolean = value
    fun set(value: Boolean) {
        this.value = value
    }

    fun getAndSet(value: Boolean): Boolean =
        inner.getAndSet(value.toInt()) == 1

    private fun Boolean.toInt(): Int =
        if (this) 1 else 0
}


/**
 * Infinite loop that reads this atomic variable and performs the specified [action] on its value.
 */
inline fun AtomicBoolean.loop(action: (Boolean) -> Unit): Nothing {
    contract { callsInPlace(action, InvocationKind.AT_LEAST_ONCE) }
    do {
        action(value)
    } while (true)
}

inline fun AtomicBoolean.tryUpdate(function: (Boolean) -> Boolean): Boolean {
    contract { callsInPlace(function, InvocationKind.EXACTLY_ONCE) }
    return tryUpdate(function) { _, _ -> }
}

inline fun AtomicBoolean.update(function: (Boolean) -> Boolean) {
    contract { callsInPlace(function, InvocationKind.AT_LEAST_ONCE) }
    update(function) { _, _ -> }
}

/**
 * Updates variable atomically using the specified [function] of its value and returns its old value.
 */
inline fun AtomicBoolean.getAndUpdate(function: (Boolean) -> Boolean): Boolean {
    contract { callsInPlace(function, InvocationKind.AT_LEAST_ONCE) }
    return update(function) { old, _ -> old }
}

/**
 * Updates variable atomically using the specified [function] of its value and returns its new value.
 */
inline fun AtomicBoolean.updateAndGet(function: (Boolean) -> Boolean): Boolean {
    contract { callsInPlace(function, InvocationKind.AT_LEAST_ONCE) }
    return update(function) { _, new -> new }
}

@PublishedApi
internal inline fun <R> AtomicBoolean.update(
    function: (Boolean) -> Boolean,
    transform: (old: Boolean, new: Boolean) -> R,
): R {
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
internal inline fun AtomicBoolean.tryUpdate(
    function: (Boolean) -> Boolean,
    onUpdated: (old: Boolean, new: Boolean) -> Unit,
): Boolean {
    contract {
        callsInPlace(function, InvocationKind.EXACTLY_ONCE)
        callsInPlace(onUpdated, InvocationKind.AT_MOST_ONCE)
    }
    val cur = value
    val upd = function(cur)
    return compareAndSet(cur, upd).also { if (it) onUpdated(cur, upd) }
}