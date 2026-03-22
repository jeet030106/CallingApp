package com.example.calling_app.model


sealed interface CallState {
    object Idle : CallState
    data class Calling(val number: String) : CallState
    data class Ringing(val callerName: String, val callerNumber: String) : CallState

    // Updated: Now holds the toggle states so the timer doesn't overwrite them
    data class Active(
        val number: String,
        val durationSeconds: Int = 0,
        val isMuted: Boolean = false,
        val isSpeakerOn: Boolean = false
    ) : CallState

    object Ended : CallState
}