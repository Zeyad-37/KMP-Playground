package com.zeyadgasser.playground.routine.data

import com.zeyadgasser.playground.routine.data.db.RoutineDao
import com.zeyadgasser.playground.routine.data.db.RoutineEntity
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RoutineRepositoryImplTest {

    // Removed: private lateinit var mockDb: RoutinesDatabase
    private lateinit var mockRoutineDao: RoutineDao
    private lateinit var routineRepository: RoutineRepository
    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    private val routineMapper = RoutineMapper // RoutineMapper is an object, used directly

    @BeforeTest
    fun setUp() {
        mockRoutineDao = mock<RoutineDao>() // Create mock for RoutineDao

        // Removed: mockDb = mock<RoutinesDatabase>()
        // Removed: everySuspend { mockDb.routineDao() } returns mockRoutineDao

        Dispatchers.setMain(testDispatcher)

        routineRepository = RoutineRepositoryImpl(
            routineDao = mockRoutineDao, // Pass the mocked DAO directly
            ioDispatcher = testDispatcher
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Sample data (assuming RoutineEntity and Routine are defined as before)
    private val sampleRoutineEntity1 = RoutineEntity(
        1L, "Morning Run", "Exercise", "07:00", "08:00", "5km run", "Fitness", false, false, null
    )
    private val sampleRoutineEntity2 =
        RoutineEntity(2L, "Read Book", "Leisure", "20:00", "21:00", "Chapter 1", "Personal", true, true, 5)
    private val sampleRoutine1: Routine = routineMapper.toDomain(sampleRoutineEntity1)
    private val sampleRoutine2: Routine = routineMapper.toDomain(sampleRoutineEntity2)

    @Test
    fun `getAllRoutines should return list of routines from DAO`() = runTest(testDispatcher) {
        // Arrange
        val entityList = listOf(sampleRoutineEntity1, sampleRoutineEntity2)
        val expectedDomainList = routineMapper.toDomainList(entityList)
        everySuspend { mockRoutineDao.getAllRoutines() } returns entityList

        // Act
        val result = routineRepository.getAllRoutines()

        // Assert
        assertEquals(expectedDomainList.size, result.size)
        assertEquals(expectedDomainList, result)
        verifySuspend(VerifyMode.exactly(1)) { mockRoutineDao.getAllRoutines() }
    }

    @Test
    fun `getAllRoutines should return empty list when DAO returns empty list`() = runTest(testDispatcher) {
        // Arrange
        everySuspend { mockRoutineDao.getAllRoutines() } returns emptyList()

        // Act
        val result = routineRepository.getAllRoutines()

        // Assert
        assertTrue(result.isEmpty(), "Result list should be empty")
        verifySuspend(VerifyMode.exactly(1)) { mockRoutineDao.getAllRoutines() }
    }

    @Test
    fun `getRoutineById should return routine when DAO finds entity`() = runTest(testDispatcher) {
        // Arrange
        val entity = sampleRoutineEntity1
        val expectedDomain = routineMapper.toDomain(entity)
        val id = entity.id
        everySuspend { mockRoutineDao.getRoutineById(id) } returns entity

        // Act
        val result = routineRepository.getRoutineById(id)

        // Assert
        assertEquals(expectedDomain, result)
        verifySuspend(VerifyMode.exactly(1)) { mockRoutineDao.getRoutineById(id) }
    }

    @Test
    fun `getRoutineById should propagate exception when DAO throws`() = runTest(testDispatcher) {
        // Arrange
        val id = 99L
        val exceptionMessage = "Database error"
        val exception = RuntimeException(exceptionMessage)
        everySuspend { mockRoutineDao.getRoutineById(any()) } throws exception // Using any() for generality

        // Act & Assert
        val thrown = assertFailsWith<RuntimeException> {
            routineRepository.getRoutineById(id)
        }
        assertEquals(exceptionMessage, thrown.message)
        verifySuspend(VerifyMode.exactly(1)) { mockRoutineDao.getRoutineById(id) }
    }

    @Test
    fun `deleteRoutine should call DAO deleteById and return result`() = runTest(testDispatcher) {
        // Arrange
        val idToDelete = 1L
        val expectedRowsAffected = 1
        everySuspend { mockRoutineDao.deleteById(idToDelete) } returns expectedRowsAffected

        // Act
        val result = routineRepository.deleteRoutine(idToDelete)

        // Assert
        assertEquals(expectedRowsAffected, result)
        verifySuspend(VerifyMode.exactly(1)) { mockRoutineDao.deleteById(idToDelete) }
    }

    @Test
    fun `deleteRoutine should return 0 when DAO returns 0 rows affected`() = runTest(testDispatcher) {
        // Arrange
        val idToDelete = 2L
        val expectedRowsAffected = 0
        everySuspend { mockRoutineDao.deleteById(idToDelete) } returns expectedRowsAffected

        // Act
        val result = routineRepository.deleteRoutine(idToDelete)

        // Assert
        assertEquals(expectedRowsAffected, result)
        verifySuspend(VerifyMode.exactly(1)) { mockRoutineDao.deleteById(idToDelete) }
    }
}