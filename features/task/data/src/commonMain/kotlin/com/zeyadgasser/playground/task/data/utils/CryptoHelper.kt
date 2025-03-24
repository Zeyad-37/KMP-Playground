package com.zeyadgasser.playground.task.data.utils

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery

@OpenForMokkery
interface CryptoHelper {
    fun decrypt(value: String): String
}
