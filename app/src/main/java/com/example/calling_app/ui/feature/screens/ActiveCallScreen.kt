package com.example.calling_app.ui.feature.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calling_app.R

@Composable
fun ActiveCallScreen(
    number: String,
    timerText: String,
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onEndCall: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top: Caller Info & Timer
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 80.dp)) {
            Text(text = number, fontSize = 36.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = timerText, fontSize = 20.sp, color = Color(0xFF4CD964)) // Green Timer
        }

        // Middle: Controls
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            CallControlButton(
                icon = if (isMuted) painterResource(R.drawable.mic_on) else painterResource(R.drawable.mic_on),
                label = "Mute",
                isActive = isMuted,
                onClick = onToggleMute
            )
            CallControlButton(
                icon = if (isSpeakerOn) painterResource(R.drawable.mic_on) else painterResource(R.drawable.mic_on),
                label = "Speaker",
                isActive = isSpeakerOn,
                onClick = onToggleSpeaker
            )
        }

        // Bottom: Hangup
        FloatingActionButton(
            onClick = onEndCall,
            containerColor = Color(0xFFFF3B30), // Red
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(painter = painterResource(R.drawable.call_end), contentDescription = "End Call", modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
fun CallControlButton(icon: Painter, label: String, isActive: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(if (isActive) Color.White else Color(0xFF2C2C2C))
                .clickable { onClick() }
        ) {
            Icon(icon, contentDescription = label, tint = if (isActive) Color.Black else Color.White, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.LightGray, fontSize = 14.sp)
    }
}