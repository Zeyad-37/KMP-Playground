package com.zeyadgasser.playground.badhabits.data.db.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.zeyadgasser.playground.badhabits.data.BadHabitsDatabase
import java.io.File

fun getBadHabitsDatabase(): BadHabitsDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "badHabits.db")
    return Room.databaseBuilder<BadHabitsDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}
