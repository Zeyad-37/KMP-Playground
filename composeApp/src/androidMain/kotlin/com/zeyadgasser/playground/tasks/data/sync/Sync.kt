package com.zeyadgasser.playground.tasks.data.sync

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object Sync {
    // This name should not be changed otherwise the app may have concurrent sync requests running
    const val SYNC_WORK_NAME = "TasksSyncWorker"

    // This method is initializes sync, the process that keeps the app's data current.
    // It is called from the app module's Application.onCreate() and should be only done once.
    fun initialize(context: Context) {
        Log.d("Sync", "enqueueUniqueWork: TasksSyncWorker")
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                TasksSyncWorker.Companion.startUpSyncWork(),
            ) // Run sync on app startup and ensure only one sync worker runs at any time
    }
}
