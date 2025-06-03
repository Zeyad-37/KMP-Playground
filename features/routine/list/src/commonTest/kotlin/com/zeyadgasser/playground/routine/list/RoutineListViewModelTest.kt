package com.zeyadgasser.playground.routine.list

import app.cash.turbine.test
import com.zeyadgasser.playground.architecture.domain.Operation
import com.zeyadgasser.playground.architecture.domain.Value
import com.zeyadgasser.playground.routine.domain.CheckRoutineUseCase
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import com.zeyadgasser.playground.routine.list.viewmodel.CategorisedRoutinePM
import com.zeyadgasser.playground.routine.list.viewmodel.CreateRoutineInput
import com.zeyadgasser.playground.routine.list.viewmodel.GoToCreateRoutineEffect
import com.zeyadgasser.playground.routine.list.viewmodel.GoToRoutineDetailsEffect
import com.zeyadgasser.playground.routine.list.viewmodel.HideDialogEffect
import com.zeyadgasser.playground.routine.list.viewmodel.HideDialogInput
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineCheckedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineClickedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.EmptyState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.ErrorState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.InitialState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.SuccessState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListViewModel
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineRatedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutinesReducer
import com.zeyadgasser.playground.routine.list.viewmodel.ShowRoutineRatingDialogEffect
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePresentationMapper
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RoutineListViewModelTest {

    // --- Centralized Test Data ---
    private val routinePM_Health_1 = RoutinePM(
        id = 1L, name = "Morning Workout", type = "Fitness",
        startTime = "07:00", endTime = "08:00", description = "Full body session",
        category = "Health", completed = false, remindersEnabled = true,
        image = "workout.png", rating = null, icon = 123
    )
    private val routinePM_PersonalGrowth_1 = RoutinePM(
        id = 2L, name = "Read Tech Articles", type = "Learning",
        startTime = "21:00", endTime = "22:00", description = "Latest on Kotlin and KMP",
        category = "Personal Growth", completed = true, remindersEnabled = false,
        image = null, rating = 5, icon = 456
    )
    private val routinePM_Health_2 = RoutinePM(
        id = 3L, name = "Evening Walk", type = "Fitness",
        startTime = "19:00", endTime = "19:30", description = "Relaxing walk",
        category = "Health", completed = false, remindersEnabled = false
    )
    private val routinePM_EmptyCategory_1 = RoutinePM(
        id = 4L, name = "General Task", type = "Misc",
        startTime = "10:00", endTime = "10:30", description = "Follow up on emails",
        category = "", // Empty string category
        completed = false, remindersEnabled = true,
        rating = null
    )

    private val routineDomain_Health_1 = Routine(
        id = 1L, name = "Morning Workout", type = "Fitness",
        startTime = "07:00", endTime = "08:00", description = "Full body session",
        category = "Health", completed = false, remindersEnabled = true, rating = null
    )
    private val routineDomain_PersonalGrowth_1 = Routine(
        id = 2L, name = "Read Tech Articles", type = "Learning",
        startTime = "21:00", endTime = "22:00", description = "Latest on Kotlin and KMP",
        category = "Personal Growth", completed = true, remindersEnabled = false, rating = 5
    )
    private val routineDomain_Health_2 = Routine(
        id = 3L, name = "Evening Walk", type = "Fitness",
        startTime = "19:00", endTime = "19:30", description = "Relaxing walk",
        category = "Health", completed = false, remindersEnabled = false
    )
    private val routineDomain_EmptyCategory_1 = Routine(
        id = 4L, name = "General Task", type = "Misc",
        startTime = "10:00", endTime = "10:30", description = "Follow up on emails",
        category = "", completed = false, remindersEnabled = true, rating = null
    )


    // --- Dependencies ---
    private val repository: RoutineRepository = mock()
    private val taskPresentationMapper: RoutinePresentationMapper = RoutinePresentationMapper
    private val checkRoutineUseCase: CheckRoutineUseCase = mock()
    private val reducer: RoutinesReducer = RoutinesReducer

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: RoutineListViewModel

    private val initialViewState = InitialState(isLoading = true)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupViewModel(initialState: RoutineListState = initialViewState) {
        viewModel = RoutineListViewModel(
            repository = repository,
            taskPresentationMapper = taskPresentationMapper,
            checkRoutineUseCase = checkRoutineUseCase,
            initialState = initialState,
            reducer = reducer,
            dispatcher = testDispatcher
        )
    }

    // --- Tests for ViewModel Inputs ---

    @Test
    fun `process LoadRoutineListInput when repository returns routines (implicit test for grouping)`() =
        runTest(testDispatcher) {
            setupViewModel()
            // These domain objects will be mapped to PM, then grouped
            val domainList = listOf(
                routineDomain_Health_1,
                routineDomain_PersonalGrowth_1,
                routineDomain_Health_2,
                routineDomain_EmptyCategory_1
            )
            // The corresponding PM list after mapping (assuming mapper maps 1:1 for these fields)
            val pmListAfterMapping = listOf(
                routinePM_Health_1,
                routinePM_PersonalGrowth_1,
                routinePM_Health_2,
                routinePM_EmptyCategory_1
            )

            // Manually determine the expected categorised list (output of the private groupIntoCategories)
            val expectedCategorisedPMList = listOf(
                CategorisedRoutinePM("Health", listOf(routinePM_Health_1, routinePM_Health_2)),
                CategorisedRoutinePM("Personal Growth", listOf(routinePM_PersonalGrowth_1)),
                CategorisedRoutinePM("", listOf(routinePM_EmptyCategory_1))
            ).sortedBy { it.category } // Sort for stable comparison if order isn't guaranteed by groupBy


            everySuspend { repository.getAllRoutines() } returns domainList
            // taskPresentationMapper.toPresentationList is called internally by the ViewModel

            viewModel.process(LoadRoutineListInput)

            viewModel.state.test {
                assertEquals(initialViewState, awaitItem())
                assertEquals(initialViewState.copy(isLoading = true), awaitItem())

                val successState = awaitItem() as SuccessState
                assertEquals(true, successState.isLoading) // From previous state if reducer works this way
                // Compare content of categorised routines, order of categories might vary from groupBy
                assertEquals(expectedCategorisedPMList.size, successState.routine.size)
                expectedCategorisedPMList.forEach { expectedCategory ->
                    val actualCategory =
                        successState.routine.find { it.category == expectedCategory.category }
                    assertNotNull(actualCategory, "Category ${expectedCategory.category} not found")
                    assertEquals(expectedCategory.routine.size, actualCategory.routine.size)
                    // Further checks for content of routines within category if necessary
                    expectedCategory.routine.forEach { expectedRoutine ->
                        assertTrue(actualCategory.routine.any { it.id == expectedRoutine.id && it.name == expectedRoutine.name },
                            "Routine ${expectedRoutine.name} not found in category ${actualCategory.category}")
                    }
                }


                // Assuming a LoadingResult(false) follows successful load for style matching
                val finalSuccessState = awaitItem() as SuccessState
                assertEquals(false, finalSuccessState.isLoading)
                // Content of routines should be the same as the previous success state
                assertEquals(expectedCategorisedPMList.size, finalSuccessState.routine.size)
                // (Similar content assertion as above for finalSuccessState.routines)

                cancelAndConsumeRemainingEvents()
            }
            verifySuspend(VerifyMode.exactly(1)) { repository.getAllRoutines() }
        }

    @Test
    fun `process LoadRoutineListInput when repository returns empty list`() = runTest(testDispatcher) {
        setupViewModel()
        everySuspend { repository.getAllRoutines() } returns emptyList()

        viewModel.process(LoadRoutineListInput)
        viewModel.state.test {
            assertEquals(initialViewState, awaitItem())
            assertEquals(EmptyState, awaitItem())
            assertEquals(SuccessState(emptyList(), true), awaitItem())
            assertEquals(SuccessState(emptyList(), false), awaitItem()) // Assuming L(F)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `process LoadRoutineListInput when repository throws error`() = runTest(testDispatcher) {
        setupViewModel()
        val exception = RuntimeException("DB error")
        everySuspend { repository.getAllRoutines() } throws exception

        viewModel.process(LoadRoutineListInput)
        viewModel.state.test {
            assertEquals(initialViewState, awaitItem())
            assertEquals(initialViewState.copy(isLoading = true), awaitItem())
            // Expecting ErrorState if VM catches and emits ExceptionResult handled by reducer
            // and a final L(F) if that's the pattern.
            assertEquals(ErrorState(exception.message!!, true), awaitItem())
            assertEquals(ErrorState(exception.message!!, false), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `process CreateRoutineInput emits GoToCreateRoutineEffect`() = runTest(testDispatcher) {
        setupViewModel()
        viewModel.process(CreateRoutineInput)
        viewModel.effect.test {
            assertEquals(GoToCreateRoutineEffect, awaitItem())
            expectNoEvents()
        }
        viewModel.state.test { assertEquals(initialViewState, expectMostRecentItem()) }
    }

    @Test
    fun `process RoutineCheckedInput emits ShowRoutineRatingDialogEffect`() = runTest(testDispatcher) {
        setupViewModel()
        val input = RoutineCheckedInput(routinePM_Health_1)
        viewModel.process(input)
        viewModel.effect.test {
            assertEquals(ShowRoutineRatingDialogEffect(routinePM_Health_1), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test { assertEquals(initialViewState, expectMostRecentItem()) }
    }

    @Test
    fun `process RoutineClickedInput emits GoToRoutineDetailsEffect`() = runTest(testDispatcher) {
        setupViewModel()
        val input = RoutineClickedInput(routinePM_PersonalGrowth_1)
        viewModel.process(input)
        viewModel.effect.test {
            assertEquals(GoToRoutineDetailsEffect(routinePM_PersonalGrowth_1.id), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test { assertEquals(initialViewState, expectMostRecentItem()) }
    }

    @Test
    fun `process HideDialogInput emits HideDialogEffect`() = runTest(testDispatcher) {
        setupViewModel()
        viewModel.process(HideDialogInput)
        viewModel.effect.test {
            assertEquals(HideDialogEffect, awaitItem())
            expectNoEvents()
        }
        viewModel.state.test { assertEquals(initialViewState, expectMostRecentItem()) }
    }

    @Test
    fun `process RoutineRatedInput when use case succeeds reloads routines`() = runTest(testDispatcher) {
        // Manually define expected initial categorized routines for this specific state
        val initialCategorisedRoutines = listOf(CategorisedRoutinePM("Health", listOf(routinePM_Health_1)))
        val initialStateForRate = SuccessState(initialCategorisedRoutines, isLoading = false)
        setupViewModel(initialState = initialStateForRate)

        val inputRoutinePM = routinePM_Health_1
        val ratingToApply = 4
        val input = RoutineRatedInput(inputRoutinePM, ratingToApply)

        val ratedRoutinePMForMapping = inputRoutinePM.copy(rating = ratingToApply)
        val domainRoutineToUpdate = taskPresentationMapper.fromPresentation(ratedRoutinePMForMapping)

        everySuspend { checkRoutineUseCase.invoke(domainRoutineToUpdate) } returns Pair(
            Operation(false),
            Value(false)
        )

        val reloadedDomainRoutines = listOf(
            routineDomain_Health_1.copy(
                rating = ratingToApply,
                completed = true,
                name = "Workout (Rated)"
            )
        )
        val reloadedPMRoutines = taskPresentationMapper.toPresentationList(reloadedDomainRoutines)
        // Manually determine the expected categorised list after reload
        val expectedCategorisedReloaded = listOf(CategorisedRoutinePM("Health", reloadedPMRoutines))


        everySuspend { repository.getAllRoutines() } returns reloadedDomainRoutines

        viewModel.process(input)

        viewModel.state.test {
            assertEquals(initialStateForRate, awaitItem())
            assertEquals(SuccessState(initialCategorisedRoutines, true), awaitItem())
            assertEquals(SuccessState(expectedCategorisedReloaded, true), awaitItem())
            assertEquals(SuccessState(expectedCategorisedReloaded, false), awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        verifySuspend(VerifyMode.exactly(1)) { checkRoutineUseCase.invoke(domainRoutineToUpdate) }
        verifySuspend(VerifyMode.exactly(1)) { repository.getAllRoutines() }
    }

    @Test
    fun `process RoutineRatedInput when use case fails does not reload`() = runTest(testDispatcher) {
        val initialCategorisedRoutines = listOf(CategorisedRoutinePM("Health", listOf(routinePM_Health_1)))
        val initialStateForRate = SuccessState(initialCategorisedRoutines, isLoading = false)
        setupViewModel(initialState = initialStateForRate)

        val inputRoutinePM = routinePM_Health_1
        val ratingToApply = 1
        val input = RoutineRatedInput(inputRoutinePM, ratingToApply)
        val ratedRoutinePMForMapping = inputRoutinePM.copy(rating = ratingToApply)
        val domainRoutineToUpdate = taskPresentationMapper.fromPresentation(ratedRoutinePMForMapping)

        everySuspend { checkRoutineUseCase.invoke(domainRoutineToUpdate) } returns Pair(
            Operation(false),
            Value(false)
        )

        viewModel.process(input)

        viewModel.state.test {
            assertEquals(initialStateForRate, awaitItem())
            assertEquals(SuccessState(initialCategorisedRoutines, true), awaitItem())
            assertEquals(SuccessState(initialCategorisedRoutines, false), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.effect.test { expectNoEvents() }
        verifySuspend(VerifyMode.exactly(1)) { checkRoutineUseCase.invoke(domainRoutineToUpdate) }
        verifySuspend(VerifyMode.exactly(0)) { repository.getAllRoutines() }
    }

    @Test
    fun `process RoutineRatedInput when use case throws error`() = runTest(testDispatcher) {
        setupViewModel() // uses default initialViewState = InitialState(true)
        val inputRoutinePM = routinePM_Health_1
        val ratingToApply = 3
        val input = RoutineRatedInput(inputRoutinePM, ratingToApply)
        val ratedRoutinePMForMapping = inputRoutinePM.copy(rating = ratingToApply)
        val domainRoutineToUpdate = taskPresentationMapper.fromPresentation(ratedRoutinePMForMapping)
        val exception = RuntimeException("Check failed")

        everySuspend { checkRoutineUseCase.invoke(domainRoutineToUpdate) } throws exception

        viewModel.process(input)

        viewModel.state.test {
            assertEquals(initialViewState, awaitItem())
            // Assuming reducer maps ExceptionResult to ErrorState(msg, previousLoadingState),
            // and then a final L(F) makes it ErrorState(msg, false)
            assertEquals(ErrorState(exception.message!!, true), awaitItem())
            assertEquals(ErrorState(exception.message!!, false), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.effect.test { expectNoEvents() }
        verifySuspend { checkRoutineUseCase.invoke(domainRoutineToUpdate) }
    }
}
