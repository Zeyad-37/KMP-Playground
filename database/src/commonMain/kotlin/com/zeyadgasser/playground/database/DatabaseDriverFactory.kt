package com.zeyadgasser.playground.database

import app.cash.sqldelight.db.SqlDriver
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery

@OpenForMokkery
interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}