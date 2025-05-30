package com.zeyadgasser.playground.routine.data.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getPersistentDatabase(): RoutinesDatabase {
    val dbFilePath = "${documentDirectory()}/routine.db"
    return Room.databaseBuilder<RoutinesDatabase>(name = dbFilePath)
        .setDriver(BundledSQLiteDriver())
        .build()
}

fun getInMemoryDatabase(): RoutinesDatabase = Room.inMemoryDatabaseBuilder<RoutinesDatabase>()
    .setDriver(BundledSQLiteDriver())
    .build()

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun documentDirectory2(): String {
    memScoped {
        val errorPtr = alloc<ObjCObjectVar<NSError?>>()
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = errorPtr.ptr,
        )
        if (documentDirectory != null) {
            return requireNotNull(documentDirectory.path) {
                """Couldn't determine the document directory.
                  URL $documentDirectory does not conform to RFC 1808.
                """.trimIndent()
            }
        } else {
            val error = errorPtr.value
            val localizedDescription = error?.localizedDescription ?: "Unknown error occurred"
            error("Couldn't determine document directory. Error: $localizedDescription")
        }
    }
}