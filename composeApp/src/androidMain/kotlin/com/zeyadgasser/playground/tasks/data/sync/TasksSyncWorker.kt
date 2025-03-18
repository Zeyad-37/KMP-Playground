package com.zeyadgasser.playground.tasks.data.sync

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import java.util.UUID
import java.util.concurrent.TimeUnit

internal class TasksSyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val topicRepository: TaskRepository,
) : CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result {
        Log.d("TasksSyncWorker", "Syncing Tasks!")
        return if (topicRepository.syncTasks())
            Result.success().also { Log.d("TasksSyncWorker", "Syncing Tasks Success!") }
        else Result.retry().also { Log.d("TasksSyncWorker", "Syncing Tasks Failed!") }
    }

    companion object {
        private const val WORKER_CLASS_NAME = "RouterWorkerDelegateClassName"

        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<TasksSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setId(UUID.randomUUID())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 7, TimeUnit.SECONDS)
            .setInitialDelay(0, TimeUnit.SECONDS)
//            .setInputData(TasksSyncWorker::class.delegatedData())
            .setInputData(
                Data.Builder()
                    .putString(WORKER_CLASS_NAME, "TasksSyncWorker")
                    .build()
            ).setConstraints(SyncConstraints)
            .build()
    }
}
