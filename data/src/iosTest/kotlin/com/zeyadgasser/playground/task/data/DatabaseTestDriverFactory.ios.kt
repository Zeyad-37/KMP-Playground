package com.zeyadgasser.playground.task.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

actual fun createTestDriver(): SqlDriver = NativeSqliteDriver(PlaygroundDB.Schema, "playground.db")
