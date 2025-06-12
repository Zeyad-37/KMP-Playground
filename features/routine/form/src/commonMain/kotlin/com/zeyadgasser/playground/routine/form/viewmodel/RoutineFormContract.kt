package com.zeyadgasser.playground.routine.form.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.routine.domain.model.Routine

sealed class RoutineFormInput : Input
data class SubmitRoutineInput(val form: RoutineForm, val routineId: Long?) : RoutineFormInput()
data object CloseRoutineFormInput : RoutineFormInput()
data class ValidateFormInput(val form: RoutineForm) : RoutineFormInput()
data class ShowTimePickerInput(val isStart: Boolean) : RoutineFormInput()
data class TimePickedInput(val time: String, val isStart: Boolean) : RoutineFormInput()
data class LoadRoutineByIdInput(val id: Long) : RoutineFormInput()

sealed class RoutineFormEffect : Effect
data object CloseCreateRoutineEffect : RoutineFormEffect()
data object HideTimePickerEffect : RoutineFormEffect()
data class ShowTimePickerEffect(val isStart: Boolean) : RoutineFormEffect()

sealed class RoutineFormState(open val form: RoutineForm) : State {
    data object InitialState : RoutineFormState(RoutineForm("", "", "", "", "", "", false))

    data class ValidationErrorState(
        val formValidation: RoutineFormValidation, override val form: RoutineForm,
    ) : RoutineFormState(form)

    data class ReadyToSubmitState(override val form: RoutineForm) : RoutineFormState(form)
}

data class RoutineForm(
    val routineName: String,
    val routineType: String,
    val routineCategory: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val remindersEnabled: Boolean,
) {
    constructor(routine: Routine) : this(
        routine.name,
        routine.type,
        routine.category,
        routine.startTime,
        routine.endTime,
        routine.description,
        routine.remindersEnabled
    )
}

data class RoutineFormValidation(
    val nameValidationErrorMessage: String?,
    val typeValidationErrorMessage: String?,
    val categoryValidationErrorMessage: String?,
    val startTimeValidationErrorMessage: String?,
    val endTimeValidationErrorMessage: String?,
    val descriptionValidationErrorMessage: String?,
)
