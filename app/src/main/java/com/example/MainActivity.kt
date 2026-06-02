package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ChatViewModel
import com.example.ui.components.TeletChatDetail
import com.example.ui.components.TeletChatList
import com.example.ui.components.TeletConfigSettings
import com.example.ui.components.TeletDrawer
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by viewModel.isDarkTheme.collectAsState()

            MyApplicationTheme(darkTheme = isDark) {
                TeletApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletApp(viewModel: ChatViewModel) {
    val drawerOpen by viewModel.drawerOpen.collectAsState()
    val activeChatId by viewModel.activeChatId.collectAsState()
    val showConfigScreen by viewModel.showConfigScreen.collectAsState()
    val chats by viewModel.filteredChats.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Sync Drawer state from ViewModel
    LaunchedEffect(drawerOpen) {
        if (drawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    // Sync back state
    LaunchedEffect(drawerState.isOpen) {
        if (drawerState.isOpen != drawerOpen) {
            viewModel.setDrawerOpen(drawerState.isOpen)
        }
    }

    val showNewGroupDialog = remember { mutableStateOf(false) }
    val showNewChatDialog = remember { mutableStateOf(false) }

    // Group dialog
    if (showNewGroupDialog.value) {
        NewChatDialog(
            titleText = "Create New Group",
            isGroup = true,
            onDismiss = { showNewGroupDialog.value = false },
            onConfirm = { title -> viewModel.createNewChat(title, isGroup = true) }
        )
    }

    // Custom chat dialog
    if (showNewChatDialog.value) {
        NewChatDialog(
            titleText = "Create New Chat",
            isGroup = false,
            onDismiss = { showNewChatDialog.value = false },
            onConfirm = { title -> viewModel.createNewChat(title, isGroup = false) }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                TeletDrawer(
                    viewModel = viewModel,
                    onCloseDrawer = { viewModel.setDrawerOpen(false) },
                    showNewGroupDialog = showNewGroupDialog,
                    showNewChatDialog = showNewChatDialog
                )
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                showConfigScreen -> {
                    TeletConfigSettings(
                        viewModel = viewModel,
                        onBack = { viewModel.setShowConfigScreen(false) }
                    )
                }
                activeChatId != null -> {
                    // Find actual selected chat
                    val activeChat = chats.find { it.id == activeChatId }
                    if (activeChat != null) {
                        TeletChatDetail(
                            viewModel = viewModel,
                            chat = activeChat,
                            onBack = { viewModel.selectChat(null) }
                        )
                    } else {
                        viewModel.selectChat(null)
                    }
                }
                else -> {
                    TeletChatList(
                        viewModel = viewModel,
                        onMenuClick = { viewModel.setDrawerOpen(true) },
                        showNewChatDialog = showNewChatDialog
                    )
                }
            }
        }
    }
}

@Composable
fun NewChatDialog(
    titleText: String,
    isGroup: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var nameInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = titleText, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = if (isGroup) "Enter the group name below:" else "Enter the contact's name below:",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_name_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameInput.isNotBlank()) {
                        onConfirm(nameInput)
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("dialog_confirm")
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel")
            ) {
                Text("Cancel")
            }
        }
    )
}
