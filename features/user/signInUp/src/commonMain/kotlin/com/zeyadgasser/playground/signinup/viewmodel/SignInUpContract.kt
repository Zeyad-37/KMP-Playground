package com.zeyadgasser.playground.signinup.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.State

sealed interface SignInUpInput : Input
data class SignInInput(val email: String, val password: String) : SignInUpInput
data class SignUpInput(val email: String, val password: String) : SignInUpInput
data object ForgotPasswordInput : SignInUpInput

sealed class SignInUpResult : Result
data class SignInResult(val message: String) : SignInUpResult()
data class SignUpResult(val message: String) : SignInUpResult()

sealed class SignInUpState(open val isLoading: Boolean) : State
data object InitialState : SignInUpState(true)
data class FailedValidationState(val message: String, override val isLoading: Boolean) : SignInUpState(false)
data class SignUpState(override val isLoading: Boolean) : SignInUpState(false)

sealed interface SignInUpEffect : Effect
data class ErrorEffect(val message: String) : SignInUpEffect
data object ForgotPasswordNavEffect : SignInUpEffect
