package com.example.calling_app.ui.feature.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calling_app.data.Contact
import com.example.calling_app.data.ContactDao
import com.example.calling_app.model.CallState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CallViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    private var timerJob: Job? = null


    fun saveContact(name: String, number: String) {
        viewModelScope.launch {
            contactDao.insertContact(Contact(phoneNumber = number, name = name))
        }
    }


    fun startAppCall(number: String) {
        if (number.isBlank()) return

        viewModelScope.launch {
            val contact = contactDao.getContactByNumber(number)
            val callerName = contact?.name

            _callState.value = CallState.Calling(number = number, name = callerName)


            delay(3000)


            if (_callState.value is CallState.Calling) {
                acceptCall(number, callerName) // Pass the name forward
            }
        }
    }

    fun simulateIncomingCall() {
        viewModelScope.launch {

            delay(2000)

            _callState.value = CallState.Ringing(
                callerName = "Test Caller",
                callerNumber = "+1 234 567 8900"
            )
        }
    }

    fun endCall() {

        timerJob?.cancel()

        viewModelScope.launch {

            _callState.value = CallState.Ended


            delay(1500)


            _callState.value = CallState.Idle
        }
    }


    fun acceptCall(number: String, name: String? = null) {
        _callState.value = CallState.Active(number = number, name = name, durationSeconds = 0)
        startTimer()
    }



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



    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var currentSeconds = 0
            while (true) {
                delay(1000)
                currentSeconds++

                val currentState = _callState.value
                if (currentState is CallState.Active) {
                    _callState.value = currentState.copy(durationSeconds = currentSeconds)
                }
            }
        }
    }
}