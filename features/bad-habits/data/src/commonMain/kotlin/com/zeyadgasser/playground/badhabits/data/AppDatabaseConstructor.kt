package com.zeyadgasser.playground.badhabits.data

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<BadHabitsDatabase> {
    override fun initialize(): BadHabitsDatabase
}