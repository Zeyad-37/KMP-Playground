package com.zeyadgasser.playground.badhabits.form.viewmodel

import com.zeyadgasser.playground.architecture.presentation.InputHandler
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.badhabits.domain.BadHabit
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

class SubmitBadHabitInputHandler(
    private val badHabitRepository: BadHabitsRepository,
    private val validateFormInputHandler: ValidateFormInputHandler,
) : InputHandler<SubmitBadHabitInput, BadHabitFormState> {
    override fun invoke(input: SubmitBadHabitInput, state: BadHabitFormState): Flow<Result> = flow {
        // TODO Add notifications if reminders are enabled
        if (input.form.name.text.isBlank() || input.form.frequency.isBlank() || input.form.description.text.isBlank())
            emitAll(validateFormInputHandler.invoke(ValidateFormInput(input.form), state))
        else {
            badHabitRepository.insertBadHabitWithRatings(with(input.form) {
                BadHabit(
                    id = input.badHabitId ?: 0,
                    name = name.text,
                    frequency = frequency,
                    description = description.text,
                    reminders = reminders,
                    creationDate = getCurrentDate(), // todo use existing date if exists
                )
            })
            emit(CloseCreateBadHabitEffect)
        }
    }
    private fun getCurrentDate(): String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        .let { "${it.date.dayOfMonth}/${it.date.month.number}/${it.date.year}" }// todo centralise in a use-case
}
