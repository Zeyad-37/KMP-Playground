package com.zeyadgasser.playground.task.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

actual fun createTestDriver(): SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { PlaygroundDB.Schema.create(it) }