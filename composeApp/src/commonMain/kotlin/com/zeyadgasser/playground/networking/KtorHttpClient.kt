package com.zeyadgasser.playground.networking

import io.github.oshai.kotlinlogging.KLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorHttpClient {

    fun json() = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
        prettyPrint = true
        encodeDefaults = true
        classDiscriminator = "#class"
    }

    fun httpClient(json: Json, kLogger: KLogger) = HttpClient {
        expectSuccess = false
        install(HttpTimeout) {
            val timeout = 10_000.toLong()
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
        install(ResponseObserver) {
            onResponse { response ->
                val body = response.bodyAsText()
                kLogger.apply {
                    debug { "HTTP status: ${response.status.value}" }
                    debug { "HTTP ResponseObserver status: $body" }
                }
            }
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    kLogger.debug { "KtorHttpClient message:$message" }
                }
            }
        }
        install(ContentNegotiation) { json(json) }
    }
}
