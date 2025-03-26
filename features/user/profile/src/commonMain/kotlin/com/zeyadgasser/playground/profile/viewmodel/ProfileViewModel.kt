package com.zeyadgasser.playground.profile.viewmodel

import com.zeyadgasser.playground.architecture.presentation.Result
import com.zeyadgasser.playground.architecture.presentation.ViewModel
import kotlinx.coroutines.flow.Flow

class ProfileViewModel(
    initialState: ProfileState = InitialState,
//    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel<ProfileInput, Result, ProfileState, ProfileEffect>(initialState) {
    override suspend fun resolve(input: ProfileInput, state: ProfileState): Flow<Result> =
        when (input) {
//            LoadProfileInput -> onLoadProfile()
            LoadProfileInput -> throw NotImplementedError()
        }

//    private fun onLoadProfile(): Flow<Result> =
//        getProfileUseCase.invoke().map { ProfileLoadedState(it.name, it.email, false) as Result }
//            .catch { emit(ErrorEffect(it.message.orEmpty()) as Result) }
}
