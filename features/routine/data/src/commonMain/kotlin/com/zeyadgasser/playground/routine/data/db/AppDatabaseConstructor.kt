package com.zeyadgasser.playground.routine.data.db

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RoutinesDatabase> {
    override fun initialize(): RoutinesDatabase
}