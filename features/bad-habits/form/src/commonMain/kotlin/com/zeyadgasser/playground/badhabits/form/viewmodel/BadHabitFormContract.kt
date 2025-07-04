package com.zeyadgasser.playground.badhabits.form.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State
import com.zeyadgasser.playground.badhabits.domain.BadHabit

sealed class BadHabitFormInput : Input
data class SubmitBadHabitInput(
    val form: BadHabitForm, val badHabitId: Long?, val creationDate: String = "",
) : BadHabitFormInput()

data object CloseBadHabitFormInput : BadHabitFormInput()
data class ValidateFormInput(val form: BadHabitForm) : BadHabitFormInput()
data class LoadBadHabitByIdInput(val id: Long) : BadHabitFormInput()

sealed class BadHabitFormEffect : Effect
data object CloseCreateBadHabitEffect : BadHabitFormEffect()

sealed class BadHabitFormState(open val form: BadHabitForm) : State {
    data object InitialState :
        BadHabitFormState(BadHabitForm(TextFieldValue(""), "", TextFieldValue(""), false))

    data class ValidationErrorState(
        val formValidation: BadHabitFormValidation, override val form: BadHabitForm,
    ) : BadHabitFormState(form)

    data class ReadyToSubmitState(override val form: BadHabitForm) : BadHabitFormState(form)
}

data class BadHabitForm(
    val name: TextFieldValue = TextFieldValue(""), // Initialize with empty TextFieldValue
    val frequency: String = "Daily", // Frequency is still a String (dropdown doesn't need TextFieldValue)
    val description: TextFieldValue = TextFieldValue(""),
    val reminders: Boolean = false,
) {
    constructor(badHabit: BadHabit) : this(
        TextFieldValue(badHabit.name),
        badHabit.frequency,
        TextFieldValue(badHabit.description),
        badHabit.reminders
    )
}

data class BadHabitFormValidation(
    val nameValidationErrorMessage: String?,
    val frequencyValidationErrorMessage: String?,
    val descriptionValidationErrorMessage: String?,
)
