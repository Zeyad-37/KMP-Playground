package com.zeyadgasser.playground.badhabits.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BadHabitEntity::class], version = 1)
abstract class BadHabitsDatabase : RoomDatabase() {
    abstract fun badHabitsDAO(): BadHabitsDao
}
