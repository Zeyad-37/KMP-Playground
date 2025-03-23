package com.zeyadgasser.playground.tasks.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import comzeyadgasserplaygroundtasksdatadb.GetAllTasksWithDependencies
import comzeyadgasserplaygroundtasksdatadb.GetTaskWithDependencies
import comzeyadgasserplaygroundtasksdatadb.PlaygroundDBQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OpenForMokkery
class PlaygroundDAO(
    private val db: PlaygroundDB,
    private val ioDispatcher: CoroutineDispatcher,
) {
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
                    dependencies?.split(",") ?: emptyList(),
                    done == 1L,
                )
            }
        }

    fun getAllTasksFlow(): Flow<List<TaskDTO>> =
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
                            dependencies?.split(",") ?: emptyList(),
                            done == 1L,
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
                    done = if (task.done == true) 1 else 0,
                )
                task.dependencies?.forEach { dbQueries.insertDependency(it, task.id) }
            }
        }
    }
}
