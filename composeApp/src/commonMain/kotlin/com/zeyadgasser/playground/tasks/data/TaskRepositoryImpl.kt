package com.zeyadgasser.playground.tasks.data

import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.IOException

class TaskRepositoryImpl(
    private val tasksAPI: TasksAPI,
//    private val tasksDB: PlaygroundDataBase,
    private val taskDataMapper: TaskDataMapper,
    override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TaskRepository {

    override fun test(): Any {
        TODO()
    }

    override suspend fun getTasks(): List<TaskDomain> =
        try {
            val tasksDTOs = syncTasksHelper()
            taskDataMapper.mapDTOsToDomains(tasksDTOs)
        } catch (e: IOException) {
            throw NotImplementedError()
//            Log.e("TaskRepository", e.message.orEmpty())
//            tasksDB.getAllTasks().map { taskDataMapper.mapEntityToDomain(it) }
        }

    override suspend fun syncTasks(): Boolean =
        try {
            syncTasksHelper().let { true }
        } catch (e: IOException) {
//            Log.e("TaskRepository", e.message.orEmpty())
            false
        }

    private suspend fun syncTasksHelper(): List<TaskDTO> =
        tasksAPI.getTasks().apply {
//            tasksDB.insertTasks(taskDataMapper.mapDTOsToEntities(this))
//            forEach { tasksDB.insertTasks(taskDataMapper.taskDependenciesFromDTO(it)) }
        }

    override fun getTasksOfflineFirst(): Flow<List<TaskDomain>> = flow { emit(getTasks()) }
//        tasksDB.getAllTasksWithDependenciesOfflineFirst()
//            .map { taskDataMapper.mapEntitiesToDomains(it) }
//            .flowOn(ioDispatcher)

    override suspend fun getTask(taskId: String): TaskDomain = throw NotImplementedError()
//        taskDataMapper.mapEntityToDomain(tasksDB.getTaskWithDependency(taskId))

    override suspend fun insertTask(task: TaskDomain): Boolean = throw NotImplementedError()
//        tasksDB.insertTasks(listOf(taskDataMapper.mapDomainToEntity(task))).let { task.done }
}
