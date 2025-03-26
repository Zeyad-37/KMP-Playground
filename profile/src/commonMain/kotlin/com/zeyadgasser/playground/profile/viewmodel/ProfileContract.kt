package com.zeyadgasser.playground.profile.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Effect
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.architecture.presentation.State

sealed interface ProfileInput : Input
data object LoadProfileInput : ProfileInput

sealed class ProfileState(open val isLoading: Boolean) : State
data object InitialState : ProfileState(true)
data class ProfileLoadedState(
    val name: String, val email: String, override val isLoading: Boolean
) : ProfileState(isLoading)

sealed interface ProfileEffect : Effect
data class ErrorEffect(val message: String) : ProfileEffect
