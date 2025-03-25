package com.zeyadgasser.playground.task.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.database.SharedDataBase
import com.zeyadgasser.playground.task.data.network.TaskDTO
import comzeyadgasserplaygroundtasksdatadb.GetAllTasksWithDependencies
import comzeyadgasserplaygroundtasksdatadb.GetTaskWithDependencies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@OpenForMokkery
class PlaygroundDAO(
    private val db: SharedDataBase,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getAllTasks(): List<TaskDTO> =
        db {
            playgroundDBQueries.getAllTasksWithDependencies().executeAsList()
                .map { result: GetAllTasksWithDependencies ->
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
        }

    fun getAllTasksFlow(): Flow<List<TaskDTO>> =
        flow { emit(db) }.flatMapConcat { db: SharedDataBase ->
            db {
                playgroundDBQueries.getAllTasksWithDependencies().asFlow().mapToList(ioDispatcher)
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
            }
        }

    suspend fun getTaskWithDependency(taskId: String): TaskDTO = db {
        playgroundDBQueries.getTaskWithDependencies(taskId).executeAsOne()
            .let { result: GetTaskWithDependencies ->
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
    }

    suspend fun insertTasks(tasks: List<TaskDTO>) = db {
        transaction {
            tasks.forEach { task: TaskDTO ->
                playgroundDBQueries.insertTask(
                    creationDate = task.creationDate,
                    dueDate = task.dueDate,
                    encryptedDescription = task.encryptedDescription,
                    encryptedTitle = task.encryptedTitle,
                    id = task.id,
                    image = task.image,
                    done = if (task.done == true) 1 else 0,
                )
                task.dependencies?.forEach { playgroundDBQueries.insertDependency(it, task.id) }
            }
        }
    }
}
