package com.zeyadgasser.playground.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB.Companion.Schema
import org.w3c.dom.Worker

class JSDatabaseDriverFactory : DatabaseDriverFactory {
    override suspend fun createDriver(): SqlDriver = WebWorkerDriver(
        Worker(
            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
        )
    ).also { Schema.create(it).await() }
}