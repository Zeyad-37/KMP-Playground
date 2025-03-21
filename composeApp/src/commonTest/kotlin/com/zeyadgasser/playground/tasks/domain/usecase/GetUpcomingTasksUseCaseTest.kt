package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUpcomingTasksUseCaseTest {
    private val useCase = GetUpcomingTasksUseCase()

    @Test
    fun `test filter out done tasks`() {
        val tasks = listOf(
            TaskDomain(
                "2023-01-01 12:00",
                "2023-01-02 12:00",
                "desc1",
                "title1",
                "id1",
                "image1",
                false,
                emptyList()
            ),
            TaskDomain(
                "2023-01-02 12:00",
                "2023-01-03 12:00",
                "desc2",
                "title2",
                "id2",
                "image2",
                true,
                emptyList()
            )
        )

        val result = useCase.invoke(tasks)

        assertEquals(1, result.size)
        assertEquals("id1", result[0].id)
    }

    @Test
    fun `test sort by creation date`() {
        val tasks = listOf(
            TaskDomain(
                "2023-01-02 12:00",
                "2023-01-03 12:00",
                "desc2",
                "title2",
                "id2",
                "image2",
                false,
                emptyList()
            ),
            TaskDomain(
                "2023-01-01 12:00",
                "2023-01-02 12:00",
                "desc1",
                "title1",
                "id1",
                "image1",
                false,
                emptyList()
            )
        )

        val result = useCase.invoke(tasks)

        assertEquals(2, result.size)
        assertEquals("id1", result[0].id)
        assertEquals("id2", result[1].id)
    }

    @Test
    fun `test topological sort`() {
        val tasks = listOf(
            TaskDomain(
                "2023-01-01 12:00",
                "2023-01-02 12:00",
                "desc1",
                "title1",
                "id1",
                "image1",
                false,
                listOf("id2")
            ),
            TaskDomain(
                "2023-01-02 12:00",
                "2023-01-03 12:00",
                "desc2",
                "title2",
                "id2",
                "image2",
                false,
                emptyList()
            )
        )

        val result = useCase.invoke(tasks)

        assertEquals(2, result.size)
        assertEquals("id2", result[0].id)
        assertEquals("id1", result[1].id)
    }

    @Test
    fun `test combined functionality`() {
        val tasks = listOf(
            TaskDomain(
                "2023-01-01 12:00",
                "2023-01-02 12:00",
                "desc1",
                "title1",
                "id1",
                "image1",
                false,
                listOf("id2")
            ),
            TaskDomain(
                "2023-01-02 12:00",
                "2023-01-03 12:00",
                "desc2",
                "title2",
                "id2",
                "image2",
                false,
                emptyList()
            ),
            TaskDomain(
                "2023-01-03 12:00",
                "2023-01-04 12:00",
                "desc3",
                "title3",
                "id3",
                "image3",
                true,
                emptyList()
            )
        )

        val result = useCase.invoke(tasks)

        assertEquals(2, result.size)
        assertEquals("id2", result[0].id)
        assertEquals("id1", result[1].id)
    }
}