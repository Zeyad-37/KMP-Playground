package com.zeyadgasser.playground.forgotpassword.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import kotlinx.coroutines.flow.Flow

class ForgotPasswordViewModel(
    initialState: ForgotPasswordState = InitialState(true),
//    private val forgotPasswordRepository: ForgotPasswordRepository,
) : ViewModel<ForgotPasswordInput, Result, ForgotPasswordState, ForgotPasswordEffect>(initialState) {
    override suspend fun resolve(
        input: ForgotPasswordInput, state: ForgotPasswordState,
    ): Flow<Result> = throw NotImplementedError()
//        when (input) {
//            is EmailInput -> flowOf(
//                forgotPasswordRepository.forgotPassword(input.email)
//                    .let { EmailSentState(false) }
//            ).catch { emit(ErrorEffect(it.message.orEmpty()) as Result) }
//        }
}
