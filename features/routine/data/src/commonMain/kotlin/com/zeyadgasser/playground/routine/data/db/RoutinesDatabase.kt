package com.zeyadgasser.playground.routine.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(entities = [RoutineEntity::class], version = 1, exportSchema = false)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoutinesDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        private const val DATABASE_NAME = "routines_database"

        @Volatile
        private var INSTANCE: RoutinesDatabase? = null // fixme

//        fun getInstance(context: Context): RoutinesDatabase =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    RoutinesDatabase::class.java,
//                    DATABASE_NAME
//                ).build().also { INSTANCE = it }
//            }
    }
}
