package com.example.calling_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
// NEW IMPORTS FOR HILT
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
// YOUR EXISTING IMPORTS
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()


                val viewModel: CallViewModel = hiltViewModel()
                val state by viewModel.callState.collectAsState()

                LaunchedEffect(state::class) {
                    when (state) {
                        is CallState.Idle -> {

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
                        is CallState.Ended -> {  }
                    }
                }

                NavHost(navController = navController, startDestination = "dial") {
                    composable("dial") {
                        DialPadScreen(
                            onAppCallClick = { viewModel.startAppCall(it) },
                            onTestIncomingClick = { viewModel.simulateIncomingCall() },
                            onSaveContact = { name, number -> viewModel.saveContact(name, number) }
                        )
                    }
                    composable("outgoing") {
                        val s = state as? CallState.Calling
                        OutgoingCallScreen(
                            number = s?.number ?: "",
                            name = s?.name,
                            onEndCall = { viewModel.endCall() }
                        )
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
                            name = s?.name,
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