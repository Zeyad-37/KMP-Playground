package com.zeyadgasser.playground.badhabits.form.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.badhabits.domain.BadHabitsRepository
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ReadyToSubmitState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BadHabitFormViewModel(
    private val badHabitRepository: BadHabitsRepository,
    private val validateFormInputHandler: ValidateFormInputHandler,
    private val submitBadHabitInputHandler: SubmitBadHabitInputHandler,
    initialState: BadHabitFormState,
    dispatcher: CoroutineDispatcher,
) : ViewModel<BadHabitFormInput, Result, BadHabitFormState, BadHabitFormEffect>(
    initialState, dispatcher = dispatcher
) {

    override suspend fun resolve(input: BadHabitFormInput, state: BadHabitFormState): Flow<Result> =
        when (input) {
            CloseBadHabitFormInput -> flowOf(CloseCreateBadHabitEffect)
            is SubmitBadHabitInput -> submitBadHabitInputHandler.invoke(input, state)
            is ValidateFormInput -> validateFormInputHandler.invoke(input, state)
            is LoadBadHabitByIdInput ->
                flowOf(ReadyToSubmitState(BadHabitForm(badHabitRepository.getBadHabitById(input.id))))
        }
}
