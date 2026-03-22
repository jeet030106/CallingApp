package com.example.calling_app.ui.feature.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call

import androidx.compose.material.icons.filled.Person
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
fun IncomingCallScreen(callerName: String, onAccept: () -> Unit, onReject: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 80.dp)) {
            Text(text = callerName, fontSize = 36.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Incoming call...", fontSize = 20.sp, color = Color.Gray)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(150.dp).background(Color(0xFF2C2C2C), CircleShape)
        ) {
            Icon(Icons.Filled.Person, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(80.dp))
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            FloatingActionButton(onClick = onReject, containerColor = Color(0xFFFF3B30), shape = CircleShape, modifier = Modifier.size(72.dp)) {
                Icon(painter = painterResource(R.drawable.call_end), contentDescription = "Reject", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            FloatingActionButton(onClick = onAccept, containerColor = Color(0xFF4CD964), shape = CircleShape, modifier = Modifier.size(72.dp)) {
                Icon(Icons.Filled.Call, contentDescription = "Accept", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }
    }
}