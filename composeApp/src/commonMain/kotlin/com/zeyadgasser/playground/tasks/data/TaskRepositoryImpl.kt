package com.zeyadgasser.playground.tasks.data

import com.zeyadgasser.playground.tasks.data.db.PlaygroundDataBase
import com.zeyadgasser.playground.tasks.data.mapper.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.playground.utils.OpenForMokkery
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.io.IOException

@OpenForMokkery
class TaskRepositoryImpl(
    private val tasksAPI: TasksAPI,
    private val tasksDB: PlaygroundDataBase,
    private val taskDataMapper: TaskDataMapper,
    override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TaskRepository {

    override suspend fun getTasks(): List<TaskDomain> =
        try {
            val tasksDTOs = syncTasksHelper()
            taskDataMapper.mapDTOsToDomains(tasksDTOs)
        } catch (e: IOException) {
            Napier.e(e.message.orEmpty(), e, "TaskRepository")
            tasksDB.getAllTasks().map { taskDataMapper.mapDTOToDomain(it, true) }
        }

    override suspend fun syncTasks(): Boolean =
        try {
            syncTasksHelper().let { true }
        } catch (e: IOException) {
            Napier.e(e.message.orEmpty(), e, "TaskRepository")
            false
        }

    private suspend fun syncTasksHelper(): List<TaskDTO> =
        tasksAPI.getTasks().apply { tasksDB.insertTasks(this) }

    override fun getTasksOfflineFirst(isAndroid: Boolean): Flow<List<TaskDomain>> =
        tasksDB.getAllTasksFlow()
            .onStart { if (!isAndroid) getTasks() }
            .map { taskDataMapper.mapDTOsToDomains(it) }
            .flowOn(ioDispatcher)

    override suspend fun getTask(taskId: String): TaskDomain =
        taskDataMapper.mapDTOToDomain(tasksDB.getTaskWithDependency(taskId), false)

    override suspend fun insertTask(task: TaskDomain): Boolean =
        tasksDB.insertTasks(listOf(taskDataMapper.mapDomainToDTO(task))).let { task.done }
}
