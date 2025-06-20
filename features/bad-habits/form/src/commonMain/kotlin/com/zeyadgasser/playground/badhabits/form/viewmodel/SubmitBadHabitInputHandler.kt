package com.zeyadgasser.playground.badhabits.form.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class SubmitBadHabitInputHandler(
    private val badHabitRepository: BadHabitsRepository,
    private val validateFormInputHandler: ValidateFormInputHandler,
) : InputHandler<SubmitBadHabitInput, BadHabitFormState> {
    override fun invoke(input: SubmitBadHabitInput, state: BadHabitFormState): Flow<Result> = flow {
        // TODO Add notifications if reminders are enabled
        if (input.form.name.isBlank() || input.form.frequency.isBlank() || input.form.description.isBlank())
            emitAll(validateFormInputHandler.invoke(ValidateFormInput(input.form), state))
        else {
            badHabitRepository.insertBadHabitWithRatings(with(input.form) {
                BadHabit(
                    id = input.badHabitId ?: 0,
                    name = name,
                    frequency = frequency,
                    description = description,
                    reminders = reminders,
                )
            })
            emit(CloseCreateBadHabitEffect)
        }
    }
}
