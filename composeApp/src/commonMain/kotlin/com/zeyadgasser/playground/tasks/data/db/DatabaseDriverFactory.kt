package com.zeyadgasser.playground.tasks.data.db

import app.cash.sqldelight.db.SqlDriver
import com.zeyadgasser.playground.utils.OpenForMokkery

@OpenForMokkery
interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
