package com.zeyadgasser.playground.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    override suspend fun createDriver(): SqlDriver =
        AndroidSqliteDriver(PlaygroundDB.Schema, context, name = "playground.db")
}
