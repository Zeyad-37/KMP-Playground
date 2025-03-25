package com.zeyadgasser.playground.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

class DesktopDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:playground.db").also { PlaygroundDB.Schema.create(it) }
}
