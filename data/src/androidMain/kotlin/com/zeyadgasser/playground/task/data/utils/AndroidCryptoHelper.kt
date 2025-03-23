package com.zeyadgasser.playground.task.data.utils

import ch.protonmail.android.crypto.CryptoLib

class AndroidCryptoHelper(val instance: CryptoLib = CryptoLib()) : CryptoHelper {
    override fun decrypt(value: String): String = instance.decrypt(value).getOrDefault("error")
}
