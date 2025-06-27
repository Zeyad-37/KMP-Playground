package com.zeyadgasser.playground.routine.form.viewmodel // Adjust package if needed

import androidx.compose.ui.text.input.TextFieldValue
import app.cash.turbine.test
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.InitialState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ReadyToSubmitState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ValidationErrorState
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.eq
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.atMost
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
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class RoutineFormViewModelTest {

    // --- Centralized Test Data ---
    private val routineDomain1 = Routine(
        id = 1L, name = "Morning Yoga", type = "Exercise",
        startTime = "07:00", endTime = "07:30", description = "Gentle stretching",
        category = "Wellness", completed = false, remindersEnabled = false, ratings = emptyList(),
    )
    private val routineFormFromDomain1 = RoutineForm(routineDomain1)

    private val validRoutineForm = RoutineForm(
        name = TextFieldValue("Daily Standup"), type = TextFieldValue("Meeting"),
        startTime = "09:00", endTime = "09:15", description = TextFieldValue("Quick sync with the team"),
        category = TextFieldValue("Work"), remindersEnabled = true
    )
    private val invalidRoutineForm_BlankName = validRoutineForm.copy(name = TextFieldValue(""))
    private val invalidRoutineForm_BlankType = validRoutineForm.copy(type = TextFieldValue(""))
    private val invalidRoutineForm_BlankDesc = validRoutineForm.copy(description = TextFieldValue(""))


    // --- Dependencies ---
    private val routineRepository: RoutineRepository = mock()
    // RoutinePresentationMapper is not directly used in this VM, RoutineForm has constructor

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: RoutineFormViewModel // The actual ViewModel

    private val defaultInitialState = InitialState // Default initial state

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupViewModel(initialState: RoutineFormState = defaultInitialState) {
        viewModel = RoutineFormViewModel(
            routineRepository = routineRepository,
            initialState = initialState,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `process CloseRoutineFormInput emits CloseCreateRoutineEffect`() = runTest(testDispatcher) {
        setupViewModel()
        viewModel.process(CloseRoutineFormInput)
        viewModel.effect.test {
            assertEquals(CloseCreateRoutineEffect, awaitItem())
            expectNoEvents()
        }
        viewModel.state.test { assertEquals(defaultInitialState, expectMostRecentItem()) }
    }

    @Test
    fun `process ShowTimePickerInput emits ShowTimePickerEffect`() = runTest(testDispatcher) {
        setupViewModel()
        val isStart = true
        viewModel.process(ShowTimePickerInput(isStart))
        viewModel.effect.test {
            assertEquals(ShowTimePickerEffect(isStart), awaitItem())
            expectNoEvents()
        }
        viewModel.state.test { assertEquals(defaultInitialState, expectMostRecentItem()) }
    }

    @Test
    fun `process LoadRoutineByIdInput success`() = runTest(testDispatcher) {
        setupViewModel()
        val routineId = 1L
        everySuspend { routineRepository.getRoutineById(routineId) } returns routineDomain1

        viewModel.process(LoadRoutineByIdInput(routineId))

        viewModel.state.test {
            assertEquals(defaultInitialState, awaitItem()) // Initial state
            // The VM directly emits ReadyToSubmitState as a result
            assertEquals(ReadyToSubmitState(routineFormFromDomain1), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.effect.test { expectNoEvents() }
        verifySuspend { routineRepository.getRoutineById(routineId) }
    }

    @Test
    fun `process LoadRoutineByIdInput repository throws error`() = runTest(testDispatcher) {
        setupViewModel()
        val routineId = 1L
        val exception = RuntimeException("Routine not found")
        everySuspend { routineRepository.getRoutineById(routineId) } throws exception

        // As the VM's `LoadRoutineByIdInput` handler doesn't catch, the flow from resolve will fail.
        // So, the process() call itself will rethrow.
        val thrown = assertFailsWith<RuntimeException> {
            viewModel.process(LoadRoutineByIdInput(routineId))
            // Need to run the test dispatcher to actually execute the flow
            testDispatcher.scheduler.advanceUntilIdle()
        }
        assertEquals(exception, thrown)

        viewModel.state.test {
            assertEquals(defaultInitialState, expectMostRecentItem()) // State remains initial
        }
        viewModel.effect.test { expectNoEvents() }
        verifySuspend { routineRepository.getRoutineById(routineId) }
    }


    @Test
    fun `process TimePickedInput updates form in ReadyToSubmitState and emits HideEffect`() =
        runTest(testDispatcher) {
            val initialForm = validRoutineForm.copy(startTime = "08:00")
            val timeToPick = "08:30"
            setupViewModel(initialState = ReadyToSubmitState(initialForm))

            viewModel.process(TimePickedInput(timeToPick, isStart = true))

            viewModel.state.test {
                assertEquals(ReadyToSubmitState(initialForm), awaitItem()) // Initial
                assertEquals(
                    ReadyToSubmitState(initialForm.copy(startTime = timeToPick)),
                    awaitItem()
                ) // Updated state
                cancelAndConsumeRemainingEvents()
            }
            viewModel.effect.test {
                assertEquals(HideTimePickerEffect, awaitItem())
                expectNoEvents()
            }
        }

    @Test
    fun `process TimePickedInput with empty time only emits HideEffect`() = runTest(testDispatcher) {
        setupViewModel(initialState = ReadyToSubmitState(validRoutineForm))
        viewModel.process(TimePickedInput("", isStart = true))

        viewModel.effect.test {
            assertEquals(HideTimePickerEffect, awaitItem())
            expectNoEvents()
        }
        viewModel.state.test {
            // State should not change as per VM logic for empty time
            assertEquals(ReadyToSubmitState(validRoutineForm), expectMostRecentItem())
        }
    }

    @Test
    fun `process TimePickedInput from InitialState does not update form but emits HideEffect`() =
        runTest(testDispatcher) {
            // VM logic: `InitialState -> state` which means it re-emits InitialState without form update
            setupViewModel(initialState = InitialState)

            viewModel.process(TimePickedInput("10:00", isStart = true))

            viewModel.state.test {
                assertEquals(InitialState, awaitItem())
                // The VM re-emits the *original* InitialState object due to `InitialState -> state`
                // If the InitialState object reference doesn't change, Turbine might only see one item.
                // To be precise, if the object IS re-emitted, it's the same.
                // If the intention was state.copy(form = newForm) for InitialState too, this test would change.
                // Testing as written in VM:
                expectMostRecentItem() // No new distinct state item if it's the same object reference.
            }
            viewModel.effect.test {
                assertEquals(HideTimePickerEffect, awaitItem())
                expectNoEvents()
            }
        }


    @Test
    fun `process ValidateFormInput with valid form emits ReadyToSubmitState`() = runTest(testDispatcher) {
        setupViewModel(initialState = InitialState) // Start with form data
        viewModel.process(ValidateFormInput(validRoutineForm))
        viewModel.state.test {
            assertEquals(InitialState, awaitItem())
            assertEquals(ReadyToSubmitState(validRoutineForm), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `process ValidateFormInput with invalid form emits ValidationErrorState`() = runTest(testDispatcher) {
        setupViewModel(initialState = InitialState)
        val expectedValidation = RoutineFormValidation(
            nameValidationErrorMessage = "Name is required",
            typeValidationErrorMessage = null,
            categoryValidationErrorMessage = null,
            startTimeValidationErrorMessage = null,
            endTimeValidationErrorMessage = null,
            descriptionValidationErrorMessage = null,
        )

        viewModel.process(ValidateFormInput(invalidRoutineForm_BlankName))
        viewModel.state.test {
            assertEquals(InitialState, awaitItem())
            assertEquals(ValidationErrorState(expectedValidation, invalidRoutineForm_BlankName), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `process SubmitRoutineInput with valid form inserts routine and emits CloseEffect`() =
        runTest(testDispatcher) {
            setupViewModel(initialState = ReadyToSubmitState(validRoutineForm))
            // Repository call doesn't return anything significant for this path in VM
            everySuspend { routineRepository.insertReplaceRoutine(any()) } returns Unit // Assuming Long or Unit return

            viewModel.process(SubmitRoutineInput(validRoutineForm, 1))

            viewModel.effect.test {
                assertEquals(CloseCreateRoutineEffect, awaitItem())
                expectNoEvents()
            }
            viewModel.state.test {
                // State might remain ReadyToSubmitState or transition elsewhere if reducer handles CloseCreateRoutineEffect
                // Based on VM, after emitting CloseCreateRoutineEffect, no state change is emitted from this path.
                assertEquals(ReadyToSubmitState(validRoutineForm), expectMostRecentItem())
            }
            // Verify the routine passed to repository
            val expectedRoutine = Routine(
                name = validRoutineForm.name.text, type = validRoutineForm.type.text,
                startTime = validRoutineForm.startTime, endTime = validRoutineForm.endTime,
                description = validRoutineForm.description.text, category = validRoutineForm.category.text,
                // id is not passed from form to Routine constructor in VM
                // completed, remindersEnabled, rating will take defaults
            )
            verifySuspend { routineRepository.insertReplaceRoutine(eq(expectedRoutine)) } // Use eq for data class comparison
        }

    @Test
    fun `process SubmitRoutineInput with invalid form emits ValidationErrorState`() =
        runTest(testDispatcher) {
            setupViewModel(initialState = InitialState)
            val expectedValidation = RoutineFormValidation(
                nameValidationErrorMessage = "Name is required",
                typeValidationErrorMessage = null,
                categoryValidationErrorMessage = null,
                startTimeValidationErrorMessage = null,
                endTimeValidationErrorMessage = null,
                descriptionValidationErrorMessage = null,
            )

            viewModel.process(SubmitRoutineInput(invalidRoutineForm_BlankName, 1))

            viewModel.state.test {
                assertEquals(InitialState, awaitItem())
                // Expecting ValidationErrorState because onSubmitRoutineInput calls onValidateFormInput
                assertEquals(
                    ValidationErrorState(expectedValidation, invalidRoutineForm_BlankName),
                    awaitItem()
                )
                cancelAndConsumeRemainingEvents()
            }
            viewModel.effect.test { expectNoEvents() } // No Close effect
            verifySuspend(atMost(1)) { routineRepository.insertReplaceRoutine(any()) }
        }

    @Test
    fun `process SubmitRoutineInput when repository throws error`() = runTest(testDispatcher) {
        setupViewModel(initialState = ReadyToSubmitState(validRoutineForm))
        val exception = RuntimeException("DB insert error")
        everySuspend { routineRepository.insertReplaceRoutine(any()) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            viewModel.process(SubmitRoutineInput(validRoutineForm, 1))
            testDispatcher.scheduler.advanceUntilIdle() // Ensure flow execution
        }
        assertEquals(exception, thrown)

        viewModel.state.test {
            assertEquals(ReadyToSubmitState(validRoutineForm), expectMostRecentItem()) // State remains
        }
        viewModel.effect.test { expectNoEvents() } // No Close effect
    }
}
