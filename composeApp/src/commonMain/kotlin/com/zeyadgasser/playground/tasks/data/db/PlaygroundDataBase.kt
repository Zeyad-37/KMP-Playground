package com.zeyadgasser.playground.tasks.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import comzeyadgasserplaygroundtasksdatadb.GetAllTasksWithDependencies
import comzeyadgasserplaygroundtasksdatadb.GetTaskWithDependencies
import comzeyadgasserplaygroundtasksdatadb.PlaygroundDBQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaygroundDataBase(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private val db: PlaygroundDB = PlaygroundDB(databaseDriverFactory.createDriver())
    private val dbQueries: PlaygroundDBQueries = db.playgroundDBQueries

    fun getAllTasks(): List<TaskDTO> =
        dbQueries.getAllTasksWithDependencies().executeAsList().map { result: GetAllTasksWithDependencies ->
            with(result) {
                TaskDTO(
                    creationDate,
                    dueDate,
                    encryptedDescription,
                    encryptedTitle,
                    id,
                    image,
                    dependencies?.split(",") ?: emptyList()
                )
            }
        }

    fun getAllTasksFlow(): Flow<List<TaskDTO>> = //throw NotImplementedError()
        dbQueries.getAllTasksWithDependencies().asFlow().mapToList(ioDispatcher)
            .map { result: List<GetAllTasksWithDependencies> ->
                result.map {
                    with(it) {
                        TaskDTO(
                            creationDate,
                            dueDate,
                            encryptedDescription,
                            encryptedTitle,
                            id,
                            image,
                            dependencies?.split(",") ?: emptyList()
                        )
                    }
                }
            }

    fun getTaskWithDependency(taskId: String): TaskDTO =
        dbQueries.getTaskWithDependencies(taskId).executeAsOne().let { result: GetTaskWithDependencies ->
            with(result) {
                TaskDTO(
                    creationDate,
                    dueDate,
                    encryptedDescription,
                    encryptedTitle,
                    id,
                    image,
                    dependencies?.split(",") ?: emptyList()
                )
            }
        }

    fun insertTasks(tasks: List<TaskDTO>) {
        db.transaction {
            tasks.forEach { task: TaskDTO ->
                dbQueries.insertTask(
                    creationDate = task.creationDate,
                    dueDate = task.dueDate,
                    encryptedDescription = task.encryptedDescription,
                    encryptedTitle = task.encryptedTitle,
                    id = task.id,
                    image = task.image,
                    done = 0,
                )
                task.dependencies?.forEach { dbQueries.insertDependency(it, task.id) }
            }
        }
    }
}
