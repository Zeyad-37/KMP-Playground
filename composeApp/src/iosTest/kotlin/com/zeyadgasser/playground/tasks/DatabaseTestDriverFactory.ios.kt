package com.zeyadgasser.playground.tasks

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

actual fun createTestDriver(): SqlDriver = NativeSqliteDriver(PlaygroundDB.Schema, "playground.db")
