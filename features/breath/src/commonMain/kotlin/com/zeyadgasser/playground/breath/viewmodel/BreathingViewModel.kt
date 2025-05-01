package com.zeyadgasser.playground.breath.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.ScreenModel
import com.zeyadgasser.playground.breath.model.BreathingExercise
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.ExerciseDetail
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.ExerciseList
import com.zeyadgasser.playground.breath.viewmodel.BreathingCoachScreenState.Loading
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BreathingViewModel : ViewModel(), ScreenModel {

    private val _uiState = MutableStateFlow<BreathingCoachScreenState>(Loading)
    val uiState: StateFlow<BreathingCoachScreenState> = _uiState.asStateFlow()
    private val isActive: Boolean
        get() = _uiState.value is ExerciseDetail && (_uiState.value as ExerciseDetail).isPlaying
    private var breathingCoroutine: Job? = null // Job to manage the breathing cycle coroutine

    private val allExercises: List<BreathingExercise> = loadExercises() // Implement this function

    init {
        _uiState.update { ExerciseList(allExercises) }// Initial state update to show the list after loading
    }

    fun selectExercise(exerciseId: String) {
        breathingCoroutine?.cancel() // Cancel any previous cycle if selecting a new one
        val selected = allExercises.find { it.id == exerciseId }
        selected?.let { exercise ->
            _uiState.update { ExerciseDetail(exercise = exercise, currentPhase = BreathingPhase.IDLE) }
        }
    }

    fun startExercise() {
        val currentState = _uiState.value
        if (currentState is ExerciseDetail && !currentState.isPlaying) {
            _uiState.update {
                (it as ExerciseDetail).copy(
                    isPlaying = true, currentPhase = BreathingPhase.INHALE
                )
            }
            // Start the breathing cycle coroutine
            startBreathingCycle(currentState.exercise)
            // TODO: Start actual audio playback here
        }
    }

    fun pauseExercise() {
        breathingCoroutine?.cancel() // Stop the breathing cycle
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail) {
                // TODO: Pause actual audio playback here
                currentState.copy(isPlaying = false, currentPhase = BreathingPhase.IDLE) // check this
            } else {
                currentState
            }
        }
    }

    fun stopExercise() {
        breathingCoroutine?.cancel() // Stop the breathing cycle
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail) {
                // TODO: Pause actual audio playback here
                currentState.copy(
                    isPlaying = false,
                    playbackProgress = 0f,
                    currentPhase = BreathingPhase.IDLE
                ) // check this
            } else {
                currentState
            }
        }
    }

    fun goBackToList() {
        breathingCoroutine?.cancel() // Stop the breathing cycle
        // Ensure audio is stopped if going back
        // TODO: Stop actual audio playback here
        _uiState.update { ExerciseList(allExercises) }
    }

    private fun startBreathingCycle(exercise: BreathingExercise) {
        breathingCoroutine?.cancel() // Cancel any existing cycle first
        breathingCoroutine = viewModelScope.launch {
            updatePhase(BreathingPhase.IDLE)
            delay(1000)
            // Example: Box Breathing (4-4-4-4 seconds)
            // In a real app, get timings from the 'exercise' object
            val inhaleDuration = exercise.inhaleDuration
            val holdInDuration = exercise.holdInDuration
            val exhaleDuration = exercise.exhaleDuration
            val holdOutDuration = exercise.holdOutDuration
            val fiveMinutes = exercise.defaultDuration
            var elapsedTime = 0L

            while (isActive && elapsedTime < fiveMinutes) {
                // Inhale Phase
                updatePhase(BreathingPhase.INHALE)
                var inhaleCounter: Int = inhaleDuration.toInt()
                while (isActive && inhaleCounter > 0) { //  Make isActive Observable
                    updatePhaseProgress(inhaleCounter / 1000)
                    inhaleCounter -= 1000 // Increment by 1 second++
                    delay(1000)
                    elapsedTime += 1000
                    updateProgress(elapsedTime, fiveMinutes)
                }
                if (!isActive || elapsedTime >= fiveMinutes) break

                // Hold In Phase
                updatePhase(BreathingPhase.HOLD_IN)
                var holdInCounter: Int = holdInDuration.toInt()
                while (isActive && holdInCounter > 0) { //  Make isActive Observable
                    updatePhaseProgress(holdInCounter / 1000)
                    holdInCounter -= 1000 // Increment by 1 second++
                    delay(1000)
                    elapsedTime += 1000
                    updateProgress(elapsedTime, fiveMinutes)
                }
                if (!isActive || elapsedTime >= fiveMinutes) break

                // Exhale Phase
                updatePhase(BreathingPhase.EXHALE)
                var exhaleCounter: Int = exhaleDuration.toInt()
                while (isActive && exhaleCounter > 0) { //  Make isActive Observable
                    updatePhaseProgress(exhaleCounter / 1000)
                    exhaleCounter -= 1000 // Increment by 1 second++
                    delay(1000)
                    elapsedTime += 1000
                    updateProgress(elapsedTime, fiveMinutes)
                }
                if (!isActive || elapsedTime >= fiveMinutes) break

                // Hold Out Phase
                updatePhase(BreathingPhase.HOLD_OUT)

                var holdOutCounter: Int = holdOutDuration.toInt()
                while (isActive && holdOutCounter > 0) { //  Make isActive Observable
                    updatePhaseProgress(holdOutCounter / 1000)
                    holdOutCounter -= 1000 // Increment by 1 second++
                    delay(1000)
                    elapsedTime += 1000
                    updateProgress(elapsedTime, fiveMinutes)
                }
                if (!isActive || elapsedTime >= fiveMinutes) break
            }

            // Cycle finished or stopped
            if (isActive) { // Ensure coroutine wasn't cancelled externally
                onPlaybackComplete()
            }
        }
    }

    private fun updatePhase(newPhase: BreathingPhase) {
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail && currentState.isPlaying) {
                currentState.copy(currentPhase = newPhase)
            } else {
                currentState
            }
        }
    }

    private fun updateProgress(elapsed: Long, total: Long) {
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail && currentState.isPlaying) {
                currentState.copy(playbackProgress = (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f))
            } else {
                currentState
            }
        }
    }

    private fun updatePhaseProgress(remaining: Int) {
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail && currentState.isPlaying) {
                currentState.copy(phaseProgress = remaining.toInt())
            } else {
                currentState
            }
        }
    }

    // Function to update progress from an external player callback
    fun updatePlaybackProgress(progress: Float) {
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail && currentState.isPlaying) {
                currentState.copy(playbackProgress = progress.coerceIn(0f, 1f))
            } else {
                currentState
            }
        }
    }

    // Called when the 5-minute cycle completes naturally
    private fun onPlaybackComplete() {
        breathingCoroutine?.cancel() // Ensure coroutine is stopped
        _uiState.update { currentState ->
            if (currentState is ExerciseDetail) {
                // TODO: Stop actual audio playback here
                currentState.copy(
                    isPlaying = false, playbackProgress = 0f, // Reset progress
                    currentPhase = BreathingPhase.IDLE // Reset phase
                )
            } else {
                currentState
            }
        }
    }

    private fun loadExercises(): List<BreathingExercise> {
        // TODO: Replace with actual data loading logic
        return listOf(
            BreathingExercise(
                id = "1",
                name = "Deep Breathing / Counting Breaths",
                instructions = "Inhale slowly through the nose, exhale slowly through the mouth. Can involve counting breaths (e.g., 1 on inhale, 2 on exhale, up to 5 or more). Focus on fully expanding lungs.",
                benefits = "Relaxation, calm mind and body. Signals safety to the brain. Activates the \"rest and digest\" system. Good for beginners.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 2000,
                holdOutDuration = 200,
            ), BreathingExercise(
                id = "2",
                name = "Lengthened Exhales",
                instructions = "Inhale slowly through the nose (e.g., count of 3), exhale slowly through the mouth for a longer duration (e.g., count of 6). Adjust counts as needed (e.g., 2/4, 4/8).",
                benefits = "Slows heart rate and thoughts. Calms mind and body by activating the parasympathetic nervous system. Helpful for a racing mind.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 6000,
                holdOutDuration = 600
            ), BreathingExercise(
                id = "3",
                name = "Belly Breathing (Diaphragmatic Breathing)",
                instructions = "Sit or lie down. Place one hand on the belly, one on the chest. Inhale slowly through the nose, feeling the belly push out/rise. Exhale slowly through the mouth (or nose), feeling the belly draw inwards/fall. Aim to fill the stomach with air.",
                benefits = "Relaxation, calm. Helps manage anxiety. Deeper, more mindful breath. Can help with stress and sleep.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 4000,
                holdInDuration = 400,
                exhaleDuration = 4000,
                holdOutDuration = 400
            ), BreathingExercise(
                id = "4",
                name = "Box Breathing (Square Breathing)",
                instructions = "Visualize a box. Inhale through the nose for a count of 4. Hold breath for a count of 4. Exhale through the mouth (or nose) for a count of 4. Hold breath out for a count of 4. Repeat.",
                benefits = "Helps focus. Soothes the body and relaxes. Lengthens exhales efficiently. Grounds and re-centers.",
                audioFileName = "?",
                category = "Focus",
                inhaleDuration = 4000,
                holdInDuration = 4000,
                exhaleDuration = 4000,
                holdOutDuration = 4000
            ), BreathingExercise(
                id = "5",
                name = "Alternate Nostril Breathing (Nadi Shodhana)",
                instructions = "Sit upright. Close the right nostril with the right thumb, inhale slowly through the left nostril. Pause. Close left nostril with right ring finger, release thumb, exhale through right nostril. Pause. Inhale through the right nostril. Pause. Close the right nostril with your thumb, release the ring finger, exhale through the left nostril. Pause. This is one cycle. Repeat.",
                benefits = "Balances brain hemispheres, creates a peaceful state of mind. Helps when feeling overwhelmed. Grounds the user. Calming.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 4000,
                holdInDuration = 1000,
                exhaleDuration = 4000,
                holdOutDuration = 1000
            ), BreathingExercise(
                id = "6",
                name = "Lion's Breath (Simhasana)",
                instructions = "Sit comfortably. Inhale deeply through your nose. Exhale forcefully through the open mouth with a \"Ha\" sound, sticking the tongue out and down towards the chin. Gaze can be directed upwards between eyebrows. Can involve arching back on inhale and leaning forward on exhale.",
                benefits = "Releases tension, especially in the face. Relaxing and fun. Energizing.",
                audioFileName = "?",
                category = "Energizing",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 4000,
                holdOutDuration = 400
            ), BreathingExercise(
                id = "7",
                name = "4-7-8 Breathing",
                instructions = "Inhale quietly through the nose for a count of 4. Hold the breath for a count of 7. Exhale completely through the mouth, making a whoosh sound, for a count of 8. Repeat.",
                benefits = "Releases tension, slows down, grounds the user. Relaxation.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 4000,
                holdInDuration = 7000,
                exhaleDuration = 8000,
                holdOutDuration = 0
            ), BreathingExercise(
                id = "8",
                name = "Ujjayi Breath (Victorious Breath)",
                instructions = "Sit comfortably, spine long. Inhale deeply through the nose. Exhale through the nose with lips closed, slightly constricting the back of the throat to create a soft \"ocean wave\" or \"ha\" sound.",
                benefits = "Calms the nervous system and mind. Reduces tension around the head.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 4000,
                holdOutDuration = 400
            ), BreathingExercise(
                id = "9",
                name = "Sitali Pranayama (Cooling Breath)",
                instructions = "Sit comfortably, spine long. Roll the tongue into a tube shape (if possible, otherwise gently part lips). Inhale through the rolled tongue (or mouth) as if sipping through a straw. Close mouth, hold breath briefly. Exhale normally through the nose.",
                benefits = "Cooling and humidifying effect. Benefits organs and endocrine glands. Calms the nervous system and mind.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 4000,
                holdOutDuration = 400
            ), BreathingExercise(
                id = "10",
                name = "Bhramari Pranayama (Humming Bee Breath)",
                instructions = "Sit comfortably, spine long. Gently close ears with thumbs. Place index fingers on forehead, other fingers gently resting on sides of nose/face. Keep teeth slightly apart, tongue relaxed. Inhale deeply through the nose. Exhale slowly through the nose, making a continuous, low-pitched humming sound (\"hmm\") from the throat.",
                benefits = "Calms the nervous system and mind. Reduces tension around the head.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 4000,
                holdOutDuration = 400
            ), BreathingExercise(
                id = "11",
                name = "Kapalabhati (Skull Shining Breath)",
                instructions = "Sit tall (kneeling or cross-legged). Focus on forceful, short exhalations through the nose by contracting the belly sharply. Inhalation happens passively and naturally as the belly relaxes. Start with around 30 repetitions. (Note: Practice on an empty stomach, avoid during menstruation).",
                benefits = "Energizing, boosts metabolism, helps digestion, pumps immune system. Clears the mind (implied by \"skull shining\").",
                audioFileName = "?",
                category = "Energizing",
                inhaleDuration = 3000,
                holdInDuration = 300,
                exhaleDuration = 1500,
                holdOutDuration = 150
            ), BreathingExercise(
                id = "12",
                name = "Breath of Fire 7",
                instructions = "Sit cross-legged. Raise arms in a \"V\" or \"U\" shape, shoulders relaxed. Inhale fast and forcefully through the nose, keeping arms up. Exhale fast and forcefully through the nose, bending elbows down towards hips, making fists near shoulders. Repeat inhale/exhale rhythmically 10+ times.",
                benefits = "Energizing.",
                audioFileName = "?",
                category = "Energizing",
                inhaleDuration = 1300,
                holdInDuration = 130,
                exhaleDuration = 1300,
                holdOutDuration = 130,
            ), BreathingExercise(
                id = "13",
                name = "Dirga Pranayama (Three-Part Breath)",
                instructions = "Sit or lie down. Place one hand on belly, one on rib cage (later move bottom hand to chest). Inhale deeply: first feel belly rise, then ribs expand, then chest lift. Exhale slowly: feel chest drop, ribs contract, belly fall. Breathe fully into all three areas.",
                benefits = "Increases oxygenation, providing energy (prana). Calms the mind by interrupting the anxiety feedback loop. Can reduce heart rate and blood pressure. Promotes relaxation.",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 6000,
                holdInDuration = 600,
                exhaleDuration = 7000,
                holdOutDuration = 700
            ), BreathingExercise(
                id = "14",
                name = "Breathing with Bilateral Stimulation",
                instructions = "Find a comfortable seated position. Breathe gently and deeply (e.g., inhale 4 counts, exhale 4 counts). Combine this with alternating stimulation of the right and left sides of the body (e.g., alternating sounds in headphones, though specific method not detailed in snippet).",
                benefits = "Reduces stress, clears the mind, improves focus. Quick mental refresh. Relaxation. (Note: Bilateral stimulation is often associated with EMDR therapy).",
                audioFileName = "?",
                category = "Calm",
                inhaleDuration = 4000,
                holdInDuration = 400,
                exhaleDuration = 4000,
                holdOutDuration = 400
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        breathingCoroutine?.cancel()
    }
}
