package com.zeyadgasser.playground.task.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun appDatabase(context: Context) = Room
    .databaseBuilder(context, PlaygroundDatabase::class.java, "playground.db")
    .setDriver(BundledSQLiteDriver())
    .build()