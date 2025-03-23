package com.zeyadgasser.playground.tasks.data

import com.zeyadgasser.playground.tasks.DatabaseTestDriverFactory
import com.zeyadgasser.playground.tasks.TestingData
import com.zeyadgasser.playground.tasks.data.db.PlaygroundDataBase
import com.zeyadgasser.playground.tasks.data.mapper.TaskDataMapper
import com.zeyadgasser.playground.tasks.data.network.TasksAPI
import com.zeyadgasser.playground.task.domain.TaskRepository
import com.zeyadgasser.playground.utils.CryptoHelper
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.spy
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskRepositoryImplTest {

    private val tasksAPI: TasksAPI = mock()
    private val databaseTestDriverFactory: DatabaseTestDriverFactory = DatabaseTestDriverFactory
    private val fakeDatabase = PlaygroundDataBase(databaseTestDriverFactory, StandardTestDispatcher())
    private val cryptoHelper: CryptoHelper = mock()
    private val mapper: TaskDataMapper = TaskDataMapper(cryptoHelper)
    private val spiedM: TaskDataMapper = spy<TaskDataMapper>(mapper)
    lateinit var repository: TaskRepository

    @BeforeTest
    fun setUp() {
        repository = TaskRepositoryImpl(tasksAPI, fakeDatabase, spiedM)
    }

    @Test
    fun getTasks() = runTest {
        val listDTOs = listOf(TestingData.taskFormattedDTO)
        val expected = listOf(
            TestingData.taskDomain.copy(
                dependencies = emptyList(),
                encryptedDescription = "decrypted",
                encryptedTitle = "decrypted"
            )
        )
        everySuspend { tasksAPI.getTasks() } returns listDTOs
        every { cryptoHelper.decrypt(any()) } returns "decrypted"

        assertEquals(expected, repository.getTasks())

        verifySuspend { tasksAPI.getTasks() }
        verify {
            fakeDatabase.insertTasks(
                listOf(
                    TestingData.taskDTO.copy(
                        encryptedDescription = "decrypted",
                        encryptedTitle = "decrypted"
                    )
                )
            )
        }
    }
}
