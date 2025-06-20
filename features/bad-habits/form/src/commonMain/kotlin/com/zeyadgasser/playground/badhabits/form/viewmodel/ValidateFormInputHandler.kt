package com.zeyadgasser.playground.badhabits.form.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ReadyToSubmitState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ValidationErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object ValidateFormInputHandler : InputHandler<ValidateFormInput, BadHabitFormState> {
    override fun invoke(input: ValidateFormInput, state: BadHabitFormState): Flow<Result> = flow {
        val nameValidation = if (input.form.name.isBlank()) "Name is required" else null
        val frequencyValidation = if (input.form.frequency.isBlank()) "Type is required" else null
        val descriptionValidation = if (input.form.description.isBlank()) "Description is required" else null
        if (nameValidation != null || frequencyValidation != null || descriptionValidation != null)
            emit(
                ValidationErrorState(
                    BadHabitFormValidation(nameValidation, frequencyValidation, descriptionValidation),
                    input.form
                )
            )
        else emit(ReadyToSubmitState(input.form))
    }
}
