package com.zeyadgasser.playground.architecture.utils

import kotlin.concurrent.AtomicInt

actual class AtomicInt actual constructor(initialValue: Int) {
    private val inner = AtomicInt(initialValue)

    actual fun get(): Int = inner.value

    actual fun set(newValue: Int) {
        inner.value = newValue
    }

    actual fun incrementAndGet(): Int =
        inner.incrementAndGet()

    actual fun decrementAndGet(): Int =
        inner.decrementAndGet()

    actual fun addAndGet(delta: Int): Int =
        inner.addAndGet(delta)

    actual fun compareAndSet(expected: Int, new: Int): Boolean =
        inner.compareAndSet(expected, new)

    actual fun getAndSet(value: Int): Int =
        inner.getAndSet(value)
}
