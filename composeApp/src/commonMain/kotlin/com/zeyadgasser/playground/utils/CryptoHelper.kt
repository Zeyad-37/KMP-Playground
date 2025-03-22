package com.zeyadgasser.playground.utils

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery

@OpenForMokkery
interface CryptoHelper {
    fun decrypt(value: String): String
}
