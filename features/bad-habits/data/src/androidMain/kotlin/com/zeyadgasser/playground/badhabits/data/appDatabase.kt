package com.zeyadgasser.playground.badhabits.data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun appDatabase(context: Context) = Room
    .databaseBuilder(context, BadHabitsDatabase::class.java, "badHabits.db")
    .setDriver(BundledSQLiteDriver())
    .build()