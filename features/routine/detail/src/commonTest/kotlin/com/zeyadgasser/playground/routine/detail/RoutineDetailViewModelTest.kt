package com.zeyadgasser.playground.routine.detail

import app.cash.turbine.test
import com.zeyadgasser.playground.routine.detail.viewmodel.DeleteRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.EditRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.ErrorEffect
import com.zeyadgasser.playground.routine.detail.viewmodel.GoBackInput
import com.zeyadgasser.playground.routine.detail.viewmodel.LoadRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.NavToEffect
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState.InitialState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState.SuccessState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailViewModel
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
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

@OptIn(ExperimentalCoroutinesApi::class)
class RoutineDetailViewModelTest {

    // --- Centralized Test Data (using RoutinePM/Routine from previous step) ---
    private val routinePM1 = RoutinePM(
        id = 1L, name = "Morning Workout", type = "Fitness",
        startTime = "07:00", endTime = "08:00", description = "Full body session",
        category = "Health", completed = false, remindersEnabled = true,
        image = "workout.png", rating = null, icon = 123
    )
    private val routineDomain1 = Routine(
        id = 1L, name = "Morning Workout", type = "Fitness",
        startTime = "07:00", endTime = "08:00", description = "Full body session",
        category = "Health", completed = false, remindersEnabled = true, rating = null
    )

    // --- Dependencies ---
    private val routineRepository: RoutineRepository = mock()
    private val mapper: RoutinePresentationMapper = RoutinePresentationMapper // Object

    private val testDispatcher = StandardTestDispatcher() // For runTest and ViewModel's dispatcher
    private lateinit var viewModel: RoutineDetailViewModel // The actual ViewModel

    // Define an initial state that will be used for most tests
    private val defaultInitialState = InitialState//(id = 0L, isLoading = true)


    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // For components that might use Dispatchers.Main
        // ViewModel is instantiated in each test or via a helper
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Helper to create ViewModel with specific initial state
    private fun setupViewModel(initialState: RoutineDetailState = defaultInitialState) {
        // Ensure this constructor matches your actual RoutineDetailViewModel
        viewModel = RoutineDetailViewModel(
            routineRepository = routineRepository,
            initialState = initialState,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `process LoadRoutineDetailInput success`() = runTest(testDispatcher) {
        val routineId = 1L
        val expectedSuccessState = SuccessState(routinePM1, routineId)
        setupViewModel(InitialState)

        everySuspend { routineRepository.getRoutineById(routineId) } returns routineDomain1
        // Mapper.toPresentation is called internally

        viewModel.process(LoadRoutineDetailInput(routineId))

        viewModel.state.test {
            assertEquals(InitialState, awaitItem()) // Initial state for this test
            assertEquals(expectedSuccessState, awaitItem()) // New state after loading
            cancelAndConsumeRemainingEvents()
        }
        viewModel.effect.test { expectNoEvents() } // No effects expected on successful load

        verifySuspend { routineRepository.getRoutineById(routineId) }
    }

    @Test
    fun `process LoadRoutineDetailInput when repository throws error`() = runTest(testDispatcher) {
        val routineId = 1L
        val exception = RuntimeException("Routine not found")
        setupViewModel(InitialState)

        everySuspend { routineRepository.getRoutineById(routineId) } throws exception

        viewModel.process(LoadRoutineDetailInput(routineId))

        // State should remain initial or go to an error state if VM handles it.
        // The current VM's onLoadRoutine would emit ErrorEffect if it catches.
        viewModel.state.test {
            // If onLoadRoutine doesn't change state on error, but only emits effect:
            assertEquals(InitialState, expectMostRecentItem())
        }
        viewModel.effect.test {
            // Assuming onLoadRoutine is modified to catch and emit ErrorEffect
            assertEquals(ErrorEffect("Failed to load routine: ${exception.message}"), awaitItem())
            expectNoEvents()
        }
        verifySuspend { routineRepository.getRoutineById(routineId) }
    }


    @Test
    fun `process EditRoutineDetailInput emits NavToEffect false`() = runTest(testDispatcher) {
        setupViewModel() // Uses defaultInitialState
        viewModel.process(EditRoutineDetailInput)

        viewModel.effect.test {
            assertEquals(NavToEffect(false), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test {
            assertEquals(defaultInitialState, expectMostRecentItem()) // State should not change
        }
    }

    @Test
    fun `process GoBackInput emits NavToEffect true`() = runTest(testDispatcher) {
        setupViewModel() // Uses defaultInitialState
        viewModel.process(GoBackInput)

        viewModel.effect.test {
            assertEquals(NavToEffect(true), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test {
            assertEquals(defaultInitialState, expectMostRecentItem()) // State should not change
        }
    }

    @Test
    fun `process DeleteRoutineDetailInput success`() = runTest(testDispatcher) {
        val routineIdToDelete = 1L
        // Initial state must have the ID to be deleted
        val initialStateForDelete =
            SuccessState(routinePM1.copy(id = routineIdToDelete), routineIdToDelete)
        setupViewModel(initialState = initialStateForDelete)

        everySuspend { routineRepository.deleteRoutine(routineIdToDelete) } returns 1 // Success

        viewModel.process(DeleteRoutineDetailInput)

        viewModel.effect.test {
            assertEquals(NavToEffect(true), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test {
            // State should ideally not change or go to a "deleted" state.
            // Current VM doesn't change state on delete, only emits effect.
            assertEquals(initialStateForDelete, expectMostRecentItem())
        }
        verifySuspend { routineRepository.deleteRoutine(routineIdToDelete) }
    }

    @Test
    fun `process DeleteRoutineDetailInput failure`() = runTest(testDispatcher) {
        val routineIdToDelete = 1L
        val initialStateForDelete =
            SuccessState(routinePM1.copy(id = routineIdToDelete), routineIdToDelete)
        setupViewModel(initialState = initialStateForDelete)

        everySuspend { routineRepository.deleteRoutine(routineIdToDelete) } returns 0 // Failure (0 rows affected)

        viewModel.process(DeleteRoutineDetailInput)

        viewModel.effect.test {
            assertEquals(ErrorEffect("Could not delete routine!"), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test {
            assertEquals(initialStateForDelete, expectMostRecentItem()) // State should not change
        }
        verifySuspend { routineRepository.deleteRoutine(routineIdToDelete) }
    }

    @Test
    fun `process DeleteRoutineDetailInput when repository throws error`() = runTest(testDispatcher) {
        val routineIdToDelete = 1L
        val initialStateForDelete =
            SuccessState(routinePM1.copy(id = routineIdToDelete), routineIdToDelete)
        val exception = RuntimeException("DB delete error")
        setupViewModel(initialState = initialStateForDelete)

        everySuspend { routineRepository.deleteRoutine(routineIdToDelete) } throws exception

        viewModel.process(DeleteRoutineDetailInput)

        viewModel.state.test {
            // State should remain unchanged if only effect is emitted on error
            assertEquals(initialStateForDelete, expectMostRecentItem())
        }
        viewModel.effect.test {
            // Assuming onDeleteRoutine is modified to catch and emit ErrorEffect
            assertEquals(ErrorEffect("Error deleting routine: ${exception.message}"), awaitItem())
            expectNoEvents()
        }
        verifySuspend { routineRepository.deleteRoutine(routineIdToDelete) }
    }
}