package com.zeyadgasser.playground.task.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<PlaygroundDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<PlaygroundDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun appDatabase(context: Context) = Room
    .databaseBuilder(context, PlaygroundDatabase::class.java, "my_room.db")
    .setDriver(BundledSQLiteDriver())
    .build()