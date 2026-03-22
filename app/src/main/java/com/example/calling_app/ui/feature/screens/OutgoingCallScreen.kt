package com.example.calling_app.ui.feature.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calling_app.R


@Composable
fun OutgoingCallScreen(number: String, onEndCall: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 80.dp)) {
            Text(text = number, fontSize = 36.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Calling...", fontSize = 20.sp, color = Color.Gray)
        }

        // The big green pulsing dot from your screenshot
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp).background(Color(0xFF00C853).copy(alpha = 0.2f), CircleShape)
        ) {
            Box(modifier = Modifier.size(60.dp).background(Color.White, CircleShape))
        }

        FloatingActionButton(
            onClick = onEndCall,
            containerColor = Color(0xFFFF3B30),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.call_end),
                contentDescription = "End Call",
                modifier = Modifier.size(36.dp)
            )
        }
    }
}