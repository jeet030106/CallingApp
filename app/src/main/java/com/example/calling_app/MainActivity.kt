package com.example.calling_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calling_app.model.CallState
import com.example.calling_app.ui.feature.call.CallViewModel
import com.example.calling_app.ui.feature.screens.ActiveCallScreen
import com.example.calling_app.ui.feature.screens.DialPadScreen
import com.example.calling_app.ui.feature.screens.IncomingCallScreen
import com.example.calling_app.ui.feature.screens.OutgoingCallScreen
import com.example.calling_app.util.formatDuration


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: CallViewModel = viewModel()
                val state by viewModel.callState.collectAsState()

                LaunchedEffect(state::class) {
                    when (state) {
                        is CallState.Idle -> {
                            // Check current route to prevent redundant navigation
                            if (navController.currentDestination?.route != "dial") {
                                navController.navigate("dial") { popUpTo(0) }
                            }
                        }
                        is CallState.Calling -> navController.navigate("outgoing")
                        is CallState.Ringing -> navController.navigate("incoming")
                        is CallState.Active -> {
                            if (navController.currentDestination?.route != "active") {
                                navController.navigate("active")
                            }
                        }
                        is CallState.Ended -> { /* Wait briefly to show 'Ended' state */ }
                    }
                }

                NavHost(navController = navController, startDestination = "dial") {
                    composable("dial") {
                        DialPadScreen(
                            onAppCallClick = { viewModel.startAppCall(it) },
                            onTestIncomingClick = { viewModel.simulateIncomingCall() }
                        )
                    }
                    composable("outgoing") {
                        val number = (state as? CallState.Calling)?.number ?: ""
                        OutgoingCallScreen(number = number, onEndCall = { viewModel.endCall() })
                    }
                    composable("incoming") {
                        val s = state as? CallState.Ringing
                        IncomingCallScreen(
                            callerName = s?.callerName ?: "Unknown",
                            onAccept = { viewModel.acceptCall(s?.callerNumber ?: "") },
                            onReject = { viewModel.endCall() }
                        )
                    }
                    composable("active") {
                        val s = state as? CallState.Active

                        ActiveCallScreen(
                            number = s?.number ?: "",
                            timerText = formatDuration(s?.durationSeconds ?: 0),
                            isMuted = s?.isMuted ?: false,
                            isSpeakerOn = s?.isSpeakerOn ?: false,
                            onToggleMute = { viewModel.toggleMute() },
                            onToggleSpeaker = { viewModel.toggleSpeaker() },
                            onEndCall = { viewModel.endCall() }
                        )
                    }
                }
            }
        }
    }
}