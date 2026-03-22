package com.example.calling_app.ui.feature.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calling_app.model.CallState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallViewModel : ViewModel() {
    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    private var timerJob: Job? = null

    // --- Core Call Flow Functions ---

    fun startAppCall(number: String) {
        if (number.isBlank()) return

        viewModelScope.launch {
            _callState.value = CallState.Calling(number)

            // Simulate the other person picking up the phone after 3 seconds
            delay(3000)
            // Only connect if the user hasn't hung up during those 3 seconds
            if (_callState.value is CallState.Calling) {
                acceptCall(number)
            }
        }
    }

    fun simulateIncomingCall() {
        viewModelScope.launch {
            // Wait 2 seconds before ringing so the user can see it happen
            delay(2000)
            // Transition to the Ringing state
            _callState.value = CallState.Ringing(
                callerName = "Test Caller",
                callerNumber = "+1 234 567 8900"
            )
        }
    }

    fun endCall() {
        // Stop the timer from ticking
        timerJob?.cancel()

        viewModelScope.launch {
            // Change state to Ended (UI can react to this if you have an Ended screen)
            _callState.value = CallState.Ended

            // Wait 1.5 seconds so the user can actually see that the call hung up
            delay(1500)

            // Return to the Dial Pad
            _callState.value = CallState.Idle
        }
    }

    fun acceptCall(number: String) {
        _callState.value = CallState.Active(number = number, durationSeconds = 0)
        startTimer()
    }

    // --- Toggle Functions (Mute & Speaker) ---

    fun toggleMute() {
        val currentState = _callState.value
        if (currentState is CallState.Active) {
            _callState.value = currentState.copy(isMuted = !currentState.isMuted)
        }
    }

    fun toggleSpeaker() {
        val currentState = _callState.value
        if (currentState is CallState.Active) {
            _callState.value = currentState.copy(isSpeakerOn = !currentState.isSpeakerOn)
        }
    }

    // --- Timer Logic ---

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var currentSeconds = 0
            while (true) {
                delay(1000)
                currentSeconds++
                // CRITICAL FIX: Only update the time, preserve the mute/speaker state
                val currentState = _callState.value
                if (currentState is CallState.Active) {
                    _callState.value = currentState.copy(durationSeconds = currentSeconds)
                }
            }
        }
    }
}