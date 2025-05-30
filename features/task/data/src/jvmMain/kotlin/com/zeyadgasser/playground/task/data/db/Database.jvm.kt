package com.zeyadgasser.playground.task.data.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getPlaygroundDatabase(): PlaygroundDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "playground.db")
    return Room.databaseBuilder<PlaygroundDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}
