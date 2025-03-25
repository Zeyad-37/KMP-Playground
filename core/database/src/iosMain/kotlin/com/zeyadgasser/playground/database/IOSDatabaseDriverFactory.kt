package com.zeyadgasser.playground.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override suspend fun createDriver(): SqlDriver =
        NativeSqliteDriver(PlaygroundDB.Schema, "playground.db")
}
