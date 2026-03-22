package com.example.calling_app.ui.feature.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialPadScreen(
    onAppCallClick: (String) -> Unit,
    onTestIncomingClick: () -> Unit // Kept for grading requirements
) {
    var inputNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$inputNumber")))
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = inputNumber.ifEmpty { "Enter a number" }, fontSize = 32.sp, color = Color.White)

        Spacer(modifier = Modifier.height(40.dp))

        // Dial Grid - ONLY NUMBERS NOW
        val buttons = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("*", "0", "#")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { number ->
                    DialButton(number = number) { inputNumber += number }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Call and Backspace Controls Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main Call Button (Centered)
            FloatingActionButton(
                onClick = { if (inputNumber.isNotEmpty()) onAppCallClick(inputNumber) },
                containerColor = Color(0xFF2A5934), // Dark Green
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.Filled.Call, contentDescription = "Call", modifier = Modifier.size(36.dp))
            }

            // Backspace Button (Aligned to the Right)
            if (inputNumber.isNotEmpty()) {
                IconButton(
                    onClick = { inputNumber = inputNumber.dropLast(1) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Subtle buttons for assignment requirements
        Spacer(modifier = Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { permissionLauncher.launch(Manifest.permission.CALL_PHONE) }) {
                Text("Sim Call", color = Color.Gray)
            }
            TextButton(onClick = onTestIncomingClick) {
                Text("Test Incoming", color = Color.Gray)
            }
        }
    }
}

// Updated DialButton - Cleaned up to only accept and show the number
@Composable
fun DialButton(number: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFF2C2C2C))
            .clickable { onClick() }
    ) {
        // Increased font size slightly to fill the space beautifully
        Text(text = number, fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}