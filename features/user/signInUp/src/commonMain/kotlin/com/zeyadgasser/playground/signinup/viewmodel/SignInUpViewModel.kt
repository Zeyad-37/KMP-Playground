package com.zeyadgasser.playground.signinup.viewmodel

import com.zeyadgasser.playground.architecture.presentation.ViewModel
import com.zeyadgasser.playground.architecture.presentation.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SignInUpViewModel(initialState: SignInUpState = InitialState) :
    ViewModel<SignInUpInput, SignInResult, SignInUpState, SignInUpEffect>(initialState) {
    override suspend fun resolve(input: SignInUpInput, state: SignInUpState): Flow<Result> =
        when (input) {
            ForgotPasswordInput -> flowOf(ForgotPasswordNavEffect)
            is SignInInput -> TODO()
            is SignUpInput -> TODO()
        }
}
