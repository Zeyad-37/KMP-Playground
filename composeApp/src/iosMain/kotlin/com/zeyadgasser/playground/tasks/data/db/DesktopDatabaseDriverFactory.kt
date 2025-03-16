package com.zeyadgasser.playground.tasks.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.DatabaseDriverFactory
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(PlaygroundDB.Schema, "playground.db")
    }
}
