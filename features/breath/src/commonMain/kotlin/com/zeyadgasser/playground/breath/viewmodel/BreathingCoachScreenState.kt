package com.zeyadgasser.playground.breath.viewmodel

import com.zeyadgasser.playground.breath.model.BreathingExercise

sealed interface BreathingCoachScreenState {
    object Loading : BreathingCoachScreenState
    data class ExerciseList(val exercises: List<BreathingExercise>) : BreathingCoachScreenState

    data class ExerciseDetail(
        val exercise: BreathingExercise,
        val isPlaying: Boolean = false,
        val playbackProgress: Float = 0f,
        val currentPhase: BreathingPhase = BreathingPhase.IDLE,
        val phaseProgress: Int = 0,
    ) : BreathingCoachScreenState {
        val phaseDuration: Int
            get() = when (currentPhase) {
                BreathingPhase.IDLE -> 0
                BreathingPhase.INHALE -> exercise.inhaleDuration
                BreathingPhase.HOLD_IN -> exercise.holdInDuration
                BreathingPhase.EXHALE -> exercise.exhaleDuration
                BreathingPhase.HOLD_OUT -> exercise.holdOutDuration
            }.toInt()
        val audioUri: String = exercise.audioFileName
    }
}

