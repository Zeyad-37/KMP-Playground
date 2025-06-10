package com.zeyadgasser.playground.routine.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoutineEntity::class, RoutineRatingEntity::class], version = 1, exportSchema = false)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoutinesDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun ratingDao(): RoutineRatingDao
}
