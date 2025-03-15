package com.zeyadgasser.playground

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform