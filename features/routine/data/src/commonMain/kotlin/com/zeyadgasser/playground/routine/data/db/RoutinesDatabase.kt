package com.zeyadgasser.playground.routine.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoutineEntity::class], version = 1, exportSchema = false)
abstract class RoutinesDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
