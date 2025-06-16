package com.zeyadgasser.playground.badhabits.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BadHabitEntity::class, BadHabitRatingEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class BadHabitsDatabase : RoomDatabase() {
    abstract fun badHabitsDAO(): BadHabitsDao
    abstract fun badHabitRatingDAO(): BadHabitRatingDao
}
