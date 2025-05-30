package com.zeyadgasser.playground.routine.form.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.routine.domain.RoutineRepository
import com.zeyadgasser.playground.routine.domain.model.Routine
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.InitialState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ReadyToSubmitState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ValidationErrorState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RoutineFormViewModel(
    private val routineRepository: RoutineRepository,
    initialState: RoutineFormState,
    dispatcher: CoroutineDispatcher,
) : ViewModel<RoutineFormInput, Result, RoutineFormState, RoutineFormEffect>(
    initialState, dispatcher = dispatcher
) {

    override suspend fun resolve(input: RoutineFormInput, state: RoutineFormState): Flow<Result> =
        when (input) {
            CloseRoutineFormInput -> flowOf(CloseCreateRoutineEffect)
            is SubmitRoutineInput -> onSubmitRoutineInput(input.form)
            is ValidateFormInput -> onValidateFormInput(input.form)
            is ShowTimePickerInput -> flowOf(ShowTimePickerEffect(input.isStart))
            is TimePickedInput -> onTimePickedInput(input, state)
            is LoadRoutineByIdInput ->
                flowOf(ReadyToSubmitState(RoutineForm(routineRepository.getRoutineById(input.id))))
        }

    private fun onTimePickedInput(input: TimePickedInput, state: RoutineFormState): Flow<Result> =
        flow { // TODO add chronological validation
            if (input.time.isNotEmpty()) {
                val newForm = if (input.isStart) state.form.copy(startTime = input.time)
                else state.form.copy(endTime = input.time)
                emit(
                    when (state) {
                        InitialState -> state
                        is ReadyToSubmitState -> state.copy(form = newForm)
                        is ValidationErrorState -> state.copy(form = newForm)
                    } as Result
                )
            }
            emit(HideTimePickerEffect)
        }

    private fun onValidateFormInput(form: RoutineForm): Flow<Result> = flow {
        val nameValidation = if (form.routineName.isBlank()) "Name is required" else null
        val typeValidation = if (form.routineType.isBlank()) "Type is required" else null
        val categoryValidation = if (form.routineCategory.isBlank()) "Category is required" else null
//        val startValidation = if (form.startTime.isBlank()) "Start time is required" else null
//        val endValidation = if (form.endTime.isBlank()) "End time is required" else null
        val descriptionValidation = if (form.description.isBlank()) "Description time is required" else null
        if (nameValidation != null || typeValidation != null || descriptionValidation != null)
            emit(
                ValidationErrorState(
                    RoutineFormValidation(
                        nameValidation,
                        typeValidation,
                        categoryValidation,
                        /*startValidation,
                        endValidation,*/
                        descriptionValidation
                    ), form
                )
            )
        else emit(ReadyToSubmitState(form))
    }

    private fun onSubmitRoutineInput(form: RoutineForm): Flow<Result> = flow {
        // TODO Add notifications if reminders are enabled
        if (form.routineName.isBlank() || form.routineType.isBlank() || form.description.isBlank())
            emitAll(onValidateFormInput(form))
        else {
            routineRepository.insertReplaceRoutine(with(form) {
                Routine(
                    name = routineName,
                    type = routineType,
                    startTime = startTime,
                    endTime = endTime,
                    description = description,
                    category = routineCategory,
                )
            })
            emit(CloseCreateRoutineEffect)
        }
    }
}
