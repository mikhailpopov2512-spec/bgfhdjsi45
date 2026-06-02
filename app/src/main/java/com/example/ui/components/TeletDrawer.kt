package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ChatViewModel

@Composable
fun TeletDrawer(
    viewModel: ChatViewModel,
    onCloseDrawer: () -> Unit,
    showNewGroupDialog: MutableState<Boolean>,
    showNewChatDialog: MutableState<Boolean>
) {
    val isDark by viewModel.isDarkTheme.collectAsState()
    val userPhone by viewModel.currentUserPhone.collectAsState()
    val userName by viewModel.currentUserName.collectAsState()
    val isCreator by viewModel.currentUserIsCreator.collectAsState()
    val isVerified by viewModel.currentUserIsVerified.collectAsState()

    val userInitials = remember(userName) {
        if (userName.length >= 2) {
            userName.substring(0, 2).uppercase()
        } else if (userName.isNotEmpty()) {
            userName.take(1).uppercase()
        } else {
            "TU"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Drawer Header with User Avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(if (isCreator) Color(0xFFE53935) else MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userInitials,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Night Mode quick toggle
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier.testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle Night Mode",
                            tint = if (isDark) Color(0xFFFFD54F) else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = userName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (isVerified) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF3390EC),
                            modifier = Modifier.size(16.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "✓",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = userPhone,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    if (isCreator) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF9800),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "Создатель",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

        // Drawer Menu Items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            DrawerItem(
                icon = Icons.Outlined.Group,
                label = "New Group",
                tag = "new_group_drawer_item",
                onClick = {
                    showNewGroupDialog.value = true
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.Chat,
                label = "New Chat",
                tag = "new_chat_drawer_item",
                onClick = {
                    showNewChatDialog.value = true
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.BookmarkBorder,
                label = "Saved Messages",
                tag = "saved_messages_drawer_item",
                onClick = {
                    viewModel.createNewChat("Saved Messages", isGroup = false)
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.WorkspacePremium,
                label = "Telegram Premium",
                tag = "premium_drawer_item",
                onClick = {
                    viewModel.selectSection("premium")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.Star,
                label = "Telegram Stars",
                tag = "stars_drawer_item",
                onClick = {
                    viewModel.selectSection("stars")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.CardGiftcard,
                label = "Telegram Gifts",
                tag = "gifts_drawer_item",
                onClick = {
                    viewModel.selectSection("gifts")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.BusinessCenter,
                label = "Telegram Business",
                tag = "business_drawer_item",
                onClick = {
                    viewModel.selectSection("business")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.Settings,
                label = "Credentials Configuration",
                tag = "credentials_settings_item",
                onClick = {
                    viewModel.setShowConfigScreen(true)
                    viewModel.selectSection(null)
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Outlined.Logout,
                label = "Switch Profile (Log Out)",
                tag = "logout_drawer_item",
                onClick = {
                    viewModel.performLogout()
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

            // Night Mode status indicator row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.NightsStay,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Night Mode",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Switch(
                    checked = isDark,
                    onCheckedChange = { viewModel.toggleTheme() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.testTag("drawer_night_mode_switch")
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    tag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(tag)
            .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
