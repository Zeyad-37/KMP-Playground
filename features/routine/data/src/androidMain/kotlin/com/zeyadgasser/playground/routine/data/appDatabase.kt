package com.zeyadgasser.playground.routine.data

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.zeyadgasser.playground.routine.data.db.RoutinesDatabase

fun appDatabase(context: Context) = Room
    .databaseBuilder(context, RoutinesDatabase::class.java, "routine.db")
    .setDriver(BundledSQLiteDriver())
    .build()