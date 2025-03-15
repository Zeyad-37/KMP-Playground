package com.zeyadgasser.playground.tasks.data.db

import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import comzeyadgasserplaygroundtasksdatadb.GetAllTasksWithDependencies
import comzeyadgasserplaygroundtasksdatadb.PlaygroundDBQueries
import kotlinx.coroutines.flow.Flow

class PlaygroundDataBase(databaseDriverFactory: DatabaseDriverFactory) {
    private val db: PlaygroundDB = PlaygroundDB(databaseDriverFactory.createDriver())
    private val dbQueries: PlaygroundDBQueries = db.playgroundDBQueries

    internal fun getAllTasks(): List<TaskDTO> =
        dbQueries.getAllTasksWithDependencies().executeAsList().map { result: GetAllTasksWithDependencies ->
            with(result) {
                TaskDTO(creationDate, dueDate, encryptedDescription, encryptedTitle, id, image, emptyList())
            }
        }

    fun insertTasks(tasks: List<TaskDTO>) {
        tasks.forEach { task: TaskDTO ->
            dbQueries.insertAllReplace(
                creationDate = task.creationDate,
                dueDate = task.dueDate,
                encryptedDescription = task.encryptedDescription,
                encryptedTitle = task.encryptedTitle,
                id = task.id,
                image = task.image,
                done = 0
            )
        }
    }

    fun getAllTasksWithDependenciesOfflineFirst(): Flow<List<TaskDTO>> {
        throw NotImplementedError()
//        return dbQueries.getAllTasksWithDependenciesOfflineFirst()
//            .asFlow()
//            .mapToList()
//            .map { resultList ->
//                resultList.map { result ->
//                    TaskWithDependencies(
//                        task = Task(
//                            id = result.id,
//                            creationDate = result.creationDate,
//                            dueDate = result.dueDate,
//                            encryptedDescription = result.encryptedDescription,
//                            encryptedTitle = result.encryptedTitle,
//                            image = result.image,
//                            done = result.done == 1
//                        ),
//                        dependencyIds = result.dependencyIds?.split(",") ?: emptyList()
//                    )
//                }
//            }
    }
}
