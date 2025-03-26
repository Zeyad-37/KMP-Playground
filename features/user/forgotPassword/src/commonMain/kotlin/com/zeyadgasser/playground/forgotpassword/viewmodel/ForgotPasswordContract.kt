package com.zeyadgasser.playground.forgotpassword.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State

sealed interface ForgotPasswordInput : Input
data class EmailInput(val email: String) : ForgotPasswordInput

sealed class ForgotPasswordState(open val isLoading: Boolean) : State
data class InitialState(override val isLoading: Boolean) : ForgotPasswordState(true)
data class EmailSentState(override val isLoading: Boolean) : ForgotPasswordState(false)

sealed interface ForgotPasswordEffect : Effect
data class ErrorEffect(val message: String) : ForgotPasswordEffect
