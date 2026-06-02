package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhonelinkRing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletLoginScreen(
    viewModel: ChatViewModel
) {
    var step by remember { mutableStateOf(1) } // 1: Phone prompt, 2: Verification (PIN or SMS)
    var phoneNumber by remember { mutableStateOf("+7 (999) 123-45-67") }
    var pinCodeInput by remember { mutableStateOf("") }
    var smsCodeInput by remember { mutableStateOf("") }
    var customName by remember { mutableStateOf("Telet User") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val cleanPhone = remember(phoneNumber) {
        phoneNumber.replace(" ", "").replace("-", "").replace("(", "").replace(")", "")
    }
    
    val isCreatorPhone = remember(cleanPhone) {
        cleanPhone.contains("888888888888")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telet Gateway Authenticator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (step > 1) {
                        IconButton(onClick = {
                            step = 1
                            errorMessage = null
                            pinCodeInput = ""
                            smsCodeInput = ""
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Large decorative app logo / icon container matching vibrant styling
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCreatorPhone && step > 1) Icons.Default.Security else Icons.Default.PhonelinkRing,
                        contentDescription = "Authenticator Logo",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    text = if (step == 1) "Verify Your Phone" else "Complete Security Authorization",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (step == 1) {
                        "Enter your phone number to authorize MTProto session. Standard accounts receive simulated SMS. Creator accounts (+888 888 888 888) bypass using numerical password PIN-code keys."
                    } else if (isCreatorPhone) {
                        "Welcome back, Creator. SMS bypass is active. Please authenticate using your private 4-digit digital PIN key."
                    } else {
                        "SMS code dispatched to $phoneNumber. Standard sandbox activation code: you can enter any code (e.g. 1234) containing 4 digits to sign-in."
                    },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Error alert banners
                AnimatedVisibility(visible = errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Input Controls matching active state step
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (step == 1) {
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = {
                                    phoneNumber = it
                                    errorMessage = null
                                },
                                label = { Text("Phone Number") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_phone_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            if (isCreatorPhone) {
                                OutlinedTextField(
                                    value = pinCodeInput,
                                    onValueChange = {
                                        if (it.length <= 6) {
                                            pinCodeInput = it
                                            errorMessage = null
                                        }
                                    },
                                    label = { Text("Digital PIN Code") },
                                    singleLine = true,
                                    placeholder = { Text("XXXX") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("login_pin_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            } else {
                                OutlinedTextField(
                                    value = customName,
                                    onValueChange = { customName = it },
                                    label = { Text("Display Name") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                OutlinedTextField(
                                    value = smsCodeInput,
                                    onValueChange = {
                                        if (it.length <= 6) {
                                            smsCodeInput = it
                                            errorMessage = null
                                        }
                                    },
                                    label = { Text("SMS Verification Code") },
                                    singleLine = true,
                                    placeholder = { Text("e.g. 1234") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("login_sms_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        // Submit action button
                        Button(
                            onClick = {
                                if (step == 1) {
                                    if (phoneNumber.isBlank()) {
                                        errorMessage = "Please enter a valid phone number"
                                    } else {
                                        step = 2
                                    }
                                } else {
                                    isLoading = true
                                    errorMessage = null
                                    
                                    if (isCreatorPhone) {
                                        if (pinCodeInput == "2580") {
                                            viewModel.performLogin(
                                                phone = phoneNumber,
                                                userName = "Создатель (Creator)",
                                                isCreator = true,
                                                isVerified = true
                                            )
                                        } else {
                                            isLoading = false
                                            errorMessage = "Неверный цифровой пароль! Доступ заблокирован."
                                        }
                                    } else {
                                        if (smsCodeInput.length >= 4) {
                                            val displayName = if (customName.isNotBlank()) customName else "Telet User"
                                            viewModel.performLogin(
                                                phone = phoneNumber,
                                                userName = displayName,
                                                isCreator = false,
                                                isVerified = false
                                            )
                                        } else {
                                            isLoading = false
                                            errorMessage = "Please enter a valid 4-digit verification code"
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_submit_button"),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = if (step == 1) "REQUEST GATEWAY ACCESS" else "VALIDATE AUTHENTICATION",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // Help shortcut buttons to pre-fill Creator credentials easily
                if (step == 1) {
                    TextButton(onClick = {
                        phoneNumber = "+888 888 888 888"
                        step = 2
                        errorMessage = null
                    }) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pre-fill 🛡️ Creator Credentials (+888...)")
                    }
                }
            }
        }
    }
}
