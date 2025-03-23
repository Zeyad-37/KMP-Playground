package com.zeyadgasser.playground.tasks

import app.cash.sqldelight.db.SqlDriver
import com.zeyadgasser.playground.database.DatabaseDriverFactory

object DatabaseTestDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver = createTestDriver()
}

expect fun createTestDriver(): SqlDriver
