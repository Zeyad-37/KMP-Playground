package com.zeyadgasser.playground.routine.list

import com.zeyadgasser.playground.routine.list.viewmodel.CategorisedRoutinePM
import com.zeyadgasser.playground.routine.list.viewmodel.ErrorResult
import com.zeyadgasser.playground.routine.list.viewmodel.LoadRoutineListResult
import com.zeyadgasser.playground.routine.list.viewmodel.LoadingResult
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.EmptyState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.ErrorState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.InitialState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListState.SuccessState
import com.zeyadgasser.playground.routine.list.viewmodel.RoutinesReducer
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RoutinesReducerTest {
    private val sampleRoutines = listOf(
        CategorisedRoutinePM(
            "Morning", listOf(
                RoutinePM(
                    id = 1,
                    name = "Morning Stretch",
                    type = "MORNING",
                    startTime = "01:03",
                    endTime = "01:24",
                    description = "Stretch body after waking up.",
                    completed = false,
                    category = "Morning"
                )
            )
        ),
        CategorisedRoutinePM(
            "Work", listOf(
                RoutinePM(
                    id = 1,
                    name = "Morning Stretch",
                    type = "MORNING",
                    startTime = "01:03",
                    endTime = "01:24",
                    description = "Stretch body after waking up.",
                    completed = false,
                    category = "Morning"
                )
            )
        )
    )
    private val errorMessage = "Network Error"

    // --- InitialState Transitions ---

    @Test
    fun `given InitialState(loading=T) when ErrorResult then ErrorState(loading=T)`() {
        val initialState = InitialState(isLoading = true)
        val result = ErrorResult(errorMessage)
        val newState = RoutinesReducer.reduce(result, initialState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message)
        assertTrue(newState.isLoading)
    }

    @Test
    fun `given InitialState(loading=F) when ErrorResult then ErrorState(loading=F)`() {
        val initialState = InitialState(isLoading = false)
        val result = ErrorResult(errorMessage)
        val newState = RoutinesReducer.reduce(result, initialState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message)
        assertFalse(newState.isLoading)
    }

    @Test
    fun `given InitialState when LoadingResult(T) then InitialState(loading=T)`() {
        val initialState = InitialState(isLoading = false) // Start with any
        val result = LoadingResult(isLoading = true)
        val newState = RoutinesReducer.reduce(result, initialState)

        assertTrue(newState is InitialState)
        assertTrue(newState.isLoading)
    }

    @Test
    fun `given InitialState when LoadingResult(F) then InitialState(loading=F)`() {
        val initialState = InitialState(isLoading = true) // Start with any
        val result = LoadingResult(isLoading = false)
        val newState = RoutinesReducer.reduce(result, initialState)

        assertTrue(newState is InitialState)
        assertFalse(newState.isLoading)
    }

    @Test
    fun `given InitialState(loading=T) when LoadRoutineListResult then SuccessState(loading=T)`() {
        val initialState = InitialState(isLoading = true)
        val result = LoadRoutineListResult(sampleRoutines)
        val newState = RoutinesReducer.reduce(result, initialState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine)
        assertTrue(newState.isLoading)
    }

    @Test
    fun `given InitialState(loading=F) when LoadRoutineListResult then SuccessState(loading=F)`() {
        val initialState = InitialState(isLoading = false)
        val result = LoadRoutineListResult(sampleRoutines)
        val newState = RoutinesReducer.reduce(result, initialState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine)
        assertFalse(newState.isLoading)
    }

    // --- ErrorState Transitions ---

    @Test
    fun `given ErrorState(loading=T) when ErrorResult then ErrorState(new_msg, loading=T)`() {
        val currentErrorState = ErrorState("Old Error", isLoading = true)
        val newErrorMessage = "New Network Error"
        val result = ErrorResult(newErrorMessage)
        val newState = RoutinesReducer.reduce(result, currentErrorState)

        assertTrue(newState is ErrorState)
        assertEquals(newErrorMessage, newState.message)
        assertTrue(newState.isLoading, "isLoading should be preserved from original ErrorState")
    }

    @Test
    fun `given ErrorState(loading=F) when ErrorResult then ErrorState(new_msg, loading=F)`() {
        val currentErrorState = ErrorState("Old Error", isLoading = false)
        val newErrorMessage = "New Network Error"
        val result = ErrorResult(newErrorMessage)
        val newState = RoutinesReducer.reduce(result, currentErrorState)

        assertTrue(newState is ErrorState)
        assertEquals(newErrorMessage, newState.message)
        assertFalse(newState.isLoading, "isLoading should be preserved from original ErrorState")
    }


    @Test
    fun `given ErrorState when LoadingResult(T) then ErrorState(loading=T)`() {
        val currentErrorState = ErrorState(errorMessage, isLoading = false)
        val result = LoadingResult(isLoading = true)
        val newState = RoutinesReducer.reduce(result, currentErrorState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message) // Message preserved
        assertTrue(newState.isLoading)
    }

    @Test
    fun `given ErrorState when LoadingResult(F) then ErrorState(loading=F)`() {
        val currentErrorState = ErrorState(errorMessage, isLoading = true)
        val result = LoadingResult(isLoading = false)
        val newState = RoutinesReducer.reduce(result, currentErrorState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message) // Message preserved
        assertFalse(newState.isLoading)
    }

    @Test
    fun `given ErrorState(loading=T) when LoadRoutineListResult then SuccessState(loading=T)`() {
        val currentErrorState = ErrorState(errorMessage, isLoading = true)
        val result = LoadRoutineListResult(sampleRoutines)
        val newState = RoutinesReducer.reduce(result, currentErrorState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine)
        assertTrue(newState.isLoading, "isLoading should be preserved from original ErrorState")
    }

    @Test
    fun `given ErrorState(loading=F) when LoadRoutineListResult then SuccessState(loading=F)`() {
        val currentErrorState = ErrorState(errorMessage, isLoading = false)
        val result = LoadRoutineListResult(sampleRoutines)
        val newState = RoutinesReducer.reduce(result, currentErrorState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine)
        assertFalse(newState.isLoading, "isLoading should be preserved from original ErrorState")
    }

    // --- SuccessState Transitions ---

    @Test
    fun `given SuccessState(loading=T) when ErrorResult then ErrorState(loading=T)`() {
        val currentSuccessState = SuccessState(sampleRoutines, isLoading = true)
        val result = ErrorResult(errorMessage)
        val newState = RoutinesReducer.reduce(result, currentSuccessState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message)
        assertTrue(newState.isLoading, "isLoading should be preserved from original SuccessState")
    }

    @Test
    fun `given SuccessState(loading=F) when ErrorResult then ErrorState(loading=F)`() {
        val currentSuccessState = SuccessState(sampleRoutines, isLoading = false)
        val result = ErrorResult(errorMessage)
        val newState = RoutinesReducer.reduce(result, currentSuccessState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message)
        assertFalse(newState.isLoading, "isLoading should be preserved from original SuccessState")
    }

    @Test
    fun `given SuccessState when LoadingResult(T) then SuccessState(loading=T)`() {
        val currentSuccessState = SuccessState(sampleRoutines, isLoading = false)
        val result = LoadingResult(isLoading = true)
        val newState = RoutinesReducer.reduce(result, currentSuccessState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine) // Routines preserved
        assertTrue(newState.isLoading)
    }

    @Test
    fun `given SuccessState when LoadingResult(F) then SuccessState(loading=F)`() {
        val currentSuccessState = SuccessState(sampleRoutines, isLoading = true)
        val result = LoadingResult(isLoading = false)
        val newState = RoutinesReducer.reduce(result, currentSuccessState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine) // Routines preserved
        assertFalse(newState.isLoading)
    }

    @Test
    fun `given SuccessState(loading=T) when LoadRoutineListResult then SuccessState(new_data, loading=T)`() {
        val currentSuccessState = SuccessState(
            listOf(
                CategorisedRoutinePM(
                    "Old", listOf(
                        RoutinePM(
                            id = 1,
                            name = "Morning Stretch",
                            type = "MORNING",
                            startTime = "01:03",
                            endTime = "01:24",
                            description = "Stretch body after waking up.",
                            completed = false,
                            category = "Morning"
                        )
                    )
                )
            ), isLoading = true
        )
        val newRoutines = sampleRoutines
        val result = LoadRoutineListResult(newRoutines)
        val newState = RoutinesReducer.reduce(result, currentSuccessState)

        assertTrue(newState is SuccessState)
        assertEquals(newRoutines, newState.routine)
        assertTrue(newState.isLoading, "isLoading should be preserved from original SuccessState")
    }

    @Test
    fun `given SuccessState(loading=F) when LoadRoutineListResult then SuccessState(new_data, loading=F)`() {
        val currentSuccessState = SuccessState(
            listOf(
                CategorisedRoutinePM(
                    "Old", listOf(
                        RoutinePM(
                            id = 1,
                            name = "Morning Stretch",
                            type = "MORNING",
                            startTime = "01:03",
                            endTime = "01:24",
                            description = "Stretch body after waking up.",
                            completed = false,
                            category = "Morning"
                        )
                    )
                )
            ), isLoading = false
        )
        val newRoutines = sampleRoutines
        val result = LoadRoutineListResult(newRoutines)
        val newState = RoutinesReducer.reduce(result, currentSuccessState)

        assertTrue(newState is SuccessState)
        assertEquals(newRoutines, newState.routine)
        assertFalse(newState.isLoading, "isLoading should be preserved from original SuccessState")
    }

    // --- EmptyState Transitions (EmptyState.isLoading is fixed at false) ---

    @Test
    fun `given EmptyState when ErrorResult then ErrorState(loading=F)`() {
        // EmptyState.isLoading is defined as false
        val currentState = EmptyState
        val result = ErrorResult(errorMessage)
        val newState = RoutinesReducer.reduce(result, currentState)

        assertTrue(newState is ErrorState)
        assertEquals(errorMessage, newState.message)
        assertFalse(newState.isLoading, "isLoading should be EmptyState.isLoading")
    }

    @Test
    fun `given EmptyState when LoadingResult(T) then EmptyState`() {
        // EmptyState.isLoading is defined as false. LoadingResult's isLoading is ignored.
        val currentState = EmptyState
        val result = LoadingResult(isLoading = true)
        val newState = RoutinesReducer.reduce(result, currentState)

        assertSame(EmptyState, newState, "Should return the EmptyState object itself")
        assertFalse(newState.isLoading, "EmptyState's isLoading should remain its defined value")
    }

    @Test
    fun `given EmptyState when LoadingResult(F) then EmptyState`() {
        val currentState = EmptyState
        val result = LoadingResult(isLoading = false)
        val newState = RoutinesReducer.reduce(result, currentState)

        assertSame(EmptyState, newState, "Should return the EmptyState object itself")
        assertFalse(newState.isLoading, "EmptyState's isLoading should remain its defined value")
    }

    @Test
    fun `given EmptyState when LoadRoutineListResult then SuccessState(loading=F)`() {
        // EmptyState.isLoading is defined as false
        val currentState = EmptyState
        val result = LoadRoutineListResult(sampleRoutines)
        val newState = RoutinesReducer.reduce(result, currentState)

        assertTrue(newState is SuccessState)
        assertEquals(sampleRoutines, newState.routine)
        assertFalse(newState.isLoading, "isLoading should be EmptyState.isLoading")
    }
}
