package com.zeyadgasser.playground.routine.data.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getRoutineDatabase(): RoutinesDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "routine.db")
    return Room.databaseBuilder<RoutinesDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}
