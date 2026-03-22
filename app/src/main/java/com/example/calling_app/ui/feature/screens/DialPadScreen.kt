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
import androidx.compose.material.icons.filled.PersonAdd // NEW Import
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
    onTestIncomingClick: () -> Unit,
    onSaveContact: (String, String) -> Unit
) {
    var inputNumber by remember { mutableStateOf("") }


    var showAddDialog by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$inputNumber")))
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = inputNumber.ifEmpty { "Enter a number" }, fontSize = 32.sp, color = Color.White)

            // Show the Add Contact icon only if they typed something
            if (inputNumber.isNotEmpty()) {
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.PersonAdd,
                        contentDescription = "Add Contact",
                        tint = Color(0xFF4CD964)
                    )
                }
            }
        }


        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Save Contact") },
                text = {
                    Column {
                        Text("Number: $inputNumber")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = contactName,
                            onValueChange = { contactName = it },
                            label = { Text("Contact Name") },
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (contactName.isNotBlank()) {
                            onSaveContact(contactName, inputNumber) // Save it!
                            showAddDialog = false // Close dialog
                            contactName = "" // Reset for next time
                        }
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

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


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {

            FloatingActionButton(
                onClick = { if (inputNumber.isNotEmpty()) onAppCallClick(inputNumber) },
                containerColor = Color(0xFF2A5934), // Dark Green
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.Filled.Call, contentDescription = "Call", modifier = Modifier.size(36.dp))
            }


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
        Text(text = number, fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}