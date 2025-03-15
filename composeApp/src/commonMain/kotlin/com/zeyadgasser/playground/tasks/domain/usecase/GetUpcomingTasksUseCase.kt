package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.domain.model.TaskDomain

class GetUpcomingTasksUseCase() {

    fun invoke(tasks: List<TaskDomain>): List<TaskDomain> {
        // Filter tasks that are not done
        val upcomingTasks = tasks.filter { !it.done }
        // Create a map for quick lookup of tasks by ID
        val taskMap = upcomingTasks.associateBy { it.id }

        // Topological sort function
        fun topologicalSort(tasks: List<TaskDomain>): List<TaskDomain> {
            val visited = mutableSetOf<String>()
            val result = mutableListOf<TaskDomain>()
            fun visit(task: TaskDomain) {
                if (!visited.add(task.id)) return // already visited
                for (dependencyId in task.dependencies) {
                    val dependencyTask = taskMap[dependencyId]
                    if (dependencyTask != null) {
                        visit(dependencyTask)
                    }
                }
                result.add(task)
            }
            tasks.forEach { task -> visit(task) }
            return result
        }
        // First, sort by creation date
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//        val sortedByCreationDate = upcomingTasks.sortedBy { LocalDate.parse(it.creationDate, formatter) }
//         Then, perform topological sort to respect dependencies and return
//        return topologicalSort(sortedByCreationDate)
        return emptyList() // fixme
    }
}
