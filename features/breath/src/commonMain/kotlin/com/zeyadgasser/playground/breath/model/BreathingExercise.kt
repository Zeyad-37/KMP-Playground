package com.zeyadgasser.playground.breath.model

data class BreathingExercise(
    val id: String, // Unique identifier
    val name: String, // Display name, e.g., "Box Breathing"
    val instructions: String, // Detailed instructions
    val benefits: String, // Summary of benefits
    val audioFileName: String, // Identifier for the audio file (e.g., "box_breathing_guide.mp3")
    val category: String, // e.g., "Calm", "Focus", "Sleep"
    val inhaleDuration: Long = 4000,
    val holdInDuration: Long = 4000,
    val exhaleDuration: Long = 4000,
    val holdOutDuration: Long = 4000,
    val defaultDuration: Long = 300000, // 5 minutes
//    val cycleDuration: Long = inhaleDuration + holdInDuration + exhaleDuration + holdOutDuration,
//    val numberOfCycles: Long = defaultDuration / cycleDuration,
)
