package com.zeyadgasser.playground.task.data

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.task.data.db.TasksDao
import com.zeyadgasser.playground.task.data.mapper.TaskDataMapper
import com.zeyadgasser.playground.task.data.network.TaskDTO
import com.zeyadgasser.playground.task.data.network.TasksAPI
import com.zeyadgasser.playground.task.domain.TaskRepository
import com.zeyadgasser.playground.task.domain.model.TaskDomain
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.io.IOException

@OpenForMokkery
class TaskRepositoryImpl(
    private val tasksAPI: TasksAPI,
    private val tasksDAO: TasksDao,
    private val taskDataMapper: TaskDataMapper,
    override val ioDispatcher: CoroutineDispatcher,
) : TaskRepository {

    override suspend fun getTasks(): List<TaskDomain> =
        try {
            val tasksDTOs = syncTasksHelper()
            taskDataMapper.mapDTOsToDomains(tasksDTOs)
        } catch (e: IOException) {
            tasksDAO.getAllTasksWithDependencies().map { taskDataMapper.mapEntityToDomain(it) }
        }

    override suspend fun syncTasks(): Boolean =
        try {
            syncTasksHelper().let { true }
        } catch (e: IOException) {
            Napier.e(e.message.orEmpty(), e, "TaskRepository")
            false
        }

    private suspend fun syncTasksHelper(): List<TaskDTO> =
        tasksAPI.getTasks().apply {
            tasksDAO.insertAllIgnore(taskDataMapper.mapDTOsToEntities(this))
            forEach { tasksDAO.insertAllTaskDependency(taskDataMapper.taskDependenciesFromDTO(it)) }
        }

    override fun getTasksOfflineFirst(isAndroid: Boolean): Flow<List<TaskDomain>> =
        tasksDAO.getAllTasksWithDependenciesOfflineFirst()
            .onStart { if (!isAndroid) getTasks() }
            .map { taskDataMapper.mapEntitiesToDomains(it) }
            .flowOn(ioDispatcher)

    override suspend fun getTask(taskId: String): TaskDomain =
        taskDataMapper.mapEntityToDomain(tasksDAO.getTaskWithDependency(taskId))

    override suspend fun insertTask(task: TaskDomain): Boolean =
        tasksDAO.insertAllReplace(listOf(taskDataMapper.mapDomainToEntity(task))).let { task.done }
}
