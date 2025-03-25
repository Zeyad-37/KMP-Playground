package com.zeyadgasser.playground.database

import com.zeyadgasser.playground.tasks.data.db.PlaygroundDB

class SharedDataBase(private val driverProvider: DatabaseDriverFactory) {
    private var database: PlaygroundDB? = null
    private suspend fun initDatabase() {
        if (database == null) {
            database = PlaygroundDB.invoke(driverProvider.createDriver())
        }
    }

    suspend operator fun <R> invoke(block: suspend PlaygroundDB.() -> R): R {
        initDatabase()
        return block(database!!)
    }
}