package com.zeyadgasser.playground.task.data.db

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<PlaygroundDatabase> {
    override fun initialize(): PlaygroundDatabase
}