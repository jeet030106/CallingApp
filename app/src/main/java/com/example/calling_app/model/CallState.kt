package com.example.calling_app.model

sealed interface CallState {
    object Idle : CallState

    data class Calling(
        val number: String,
        val name: String? = null
    ) : CallState

    data class Ringing(
        val callerName: String,
        val callerNumber: String
    ) : CallState

    data class Active(
        val number: String,
        val name: String? = null,
        val durationSeconds: Int = 0,
        val isMuted: Boolean = false,
        val isSpeakerOn: Boolean = false
    ) : CallState

    object Ended : CallState
}