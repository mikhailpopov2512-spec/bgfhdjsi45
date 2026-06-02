package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Custom design colors for features
val PremiumGold = Color(0xFFF9A825)
val StarBlue = Color(0xFF00B0FF)
val StarGold = Color(0xFFFFD600)
val GiftColorPink = Color(0xFFEC407A)
val BusinessColorViolet = Color(0xFF7E57C2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletPremiumScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    val isPremium by viewModel.isPremium.collectAsState()
    val isDark by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telegram Premium", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("premium_back")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Shiny Premium Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF8E24AA), Color(0xFF3949AB))
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "👑",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Telet Premium",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPremium) "ACTIVE USER LICENCE STATUS" else "UNLOCK REVOLUTIONARY POWER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                }
            }

            // Quick Toggle Panel
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isPremium) "Premium Status Active" else "Premium Status Inactive",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (isPremium) "Enjoy limitless dynamic chat features." else "Unlock supercharged MTProto, customizable avatars & badge stars.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }

                    Switch(
                        checked = isPremium,
                        onCheckedChange = { viewModel.togglePremium() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PremiumGold,
                            checkedTrackColor = PremiumGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.testTag("premium_toggle")
                    )
                }
            }

            // Exclusive Features Showcase Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Premium Benefits Showcase",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    PremiumFeatureRow(
                        icon = Icons.Default.VerifiedUser,
                        color = StarBlue,
                        title = "Exclusive Creator Badging",
                        desc = "Display verified and authority labels alongside public user metadata."
                    )

                    PremiumFeatureRow(
                        icon = Icons.Default.Speed,
                        color = Color(0xFF4CAF50),
                        title = "Server Priority Gateway",
                        desc = "Requests bypass congestion queues on MTProto live nodes."
                    )

                    PremiumFeatureRow(
                        icon = Icons.Default.WorkspacePremium,
                        color = PremiumGold,
                        title = "Gift & Sticker Multipliers",
                        desc = "Multiply star benefits and unlock premium golden-stamped sticker assets."
                    )

                    PremiumFeatureRow(
                        icon = Icons.Default.AutoAwesome,
                        color = Color(0xFFAB47BC),
                        title = "Infinite Custom Themes",
                        desc = "Saturate UI with highly vibrant designs, border outlines and dynamic fonts."
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumFeatureRow(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, title: String, desc: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.15f),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletStarsScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    val balance by viewModel.starsBalance.collectAsState()
    var showPurchaseSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telegram Stars", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("stars_back")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Golden Star Card Balance
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFFA000), Color(0xFFF57C00))
                        )
                    )
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars Icon",
                        tint = StarGold,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "$balance",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "CURRENT TELEGRAM STARS BALANCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                }
            }

            AnimatedVisibility(visible = showPurchaseSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                        Text(
                            text = "Stars added successfully to your wallet!",
                            color = Color(0xFF2E7D32),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Buy Showcase Packs
            Text(
                text = "Replenish Balance Pack List",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )

            StarPackRow(stars = 100, price = "$1.99", onBuy = {
                viewModel.addStars(100)
                showPurchaseSuccess = true
            })
            StarPackRow(stars = 250, price = "$4.99", onBuy = {
                viewModel.addStars(250)
                showPurchaseSuccess = true
            })
            StarPackRow(stars = 1000, price = "$18.99", onBuy = {
                viewModel.addStars(1000)
                showPurchaseSuccess = true
            })
            StarPackRow(stars = 5000, price = "$89.99", onBuy = {
                viewModel.addStars(5000)
                showPurchaseSuccess = true
            })

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StarPackRow(stars: Int, price: String, onBuy: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$stars Stars Pack",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Button(
                onClick = onBuy,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Buy for $price", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletBusinessScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    val isBusiness by viewModel.isBusiness.collectAsState()
    val greeting by viewModel.businessGreeting.collectAsState()
    val address by viewModel.businessAddress.collectAsState()

    var editingGreeting by remember { mutableStateOf("") }
    var editingAddress by remember { mutableStateOf("") }
    var showBusinessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(isBusiness, greeting, address) {
        editingGreeting = greeting
        editingAddress = address
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telegram Business", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("business_back")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Elegant Business Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF673AB7), Color(0xFFEF5350))
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BusinessCenter,
                        contentDescription = "Business Logo",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Telet Business Suite",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "PRECISE CONTROL FOR CREATORS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                }
            }

            AnimatedVisibility(visible = showBusinessMessage) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Business Profile data updated successfully!",
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Quick Business Status Config Box
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Business Mode Status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Enables automated custom greeting & official address.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }

                        Switch(
                            checked = isBusiness,
                            onCheckedChange = { viewModel.updateBusinessInfo(it, editingGreeting, editingAddress) },
                            modifier = Modifier.testTag("business_toggle")
                        )
                    }

                    if (isBusiness) {
                        Divider()

                        OutlinedTextField(
                            value = editingGreeting,
                            onValueChange = { editingGreeting = it },
                            label = { Text("Automated Welcome Message") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("business_greeting_input")
                        )

                        OutlinedTextField(
                            value = editingAddress,
                            onValueChange = { editingAddress = it },
                            label = { Text("Official Business Address") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("business_address_input")
                        )

                        Button(
                            onClick = {
                                viewModel.updateBusinessInfo(true, editingGreeting, editingAddress)
                                showBusinessMessage = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_business_button")
                        ) {
                            Text("SAVE BUSINESS DETAILS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletGiftsScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    val starsCount by viewModel.starsBalance.collectAsState()
    val userSavedGifts by viewModel.giftsList.collectAsState()
    var purchaseResult by remember { mutableStateOf<String?>(null) }

    val showCaseGifts = listOf(
        ShowCaseGift("🔥 Fire Star", "Fire", 150),
        ShowCaseGift("❤️ Premium Ruby Heart", "Heart", 200),
        ShowCaseGift("🔔 Golden bell", "Bell", 350),
        ShowCaseGift("💎 Azure Diamond", "Diamond", 500)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Telegram Star Gifts", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("gifts_back")) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Elegant Gifts Showcase Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFEC407A), Color(0xFFFF7043))
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎁",
                        fontSize = 44.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = "Digital Star Gifts Room",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "SEND EXCELLENCE TO COMPANIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                }
            }

            // Wallet overview box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Coin Stars Balance", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$starsCount Stars", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    TextButton(onClick = { viewModel.addStars(500) }) {
                        Text("+ Add Stars")
                    }
                }
            }

            AnimatedVisibility(visible = purchaseResult != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (purchaseResult?.startsWith("Error") == true) MaterialTheme.colorScheme.errorContainer else Color(0xFFE8F5E9)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = purchaseResult ?: "",
                        modifier = Modifier.padding(12.dp),
                        color = if (purchaseResult?.startsWith("Error") == true) MaterialTheme.colorScheme.onErrorContainer else Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            // Gift showcase grid titles
            Text(
                text = "Buy & Dispatch Available Gifts Showcase",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Dynamic grid list of Showcase Gifts
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                showCaseGifts.forEach { gift ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(GiftColorPink.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when(gift.iconType) {
                                            "Fire" -> "🔥"
                                            "Heart" -> "❤️"
                                            "Bell" -> "🔔"
                                            else -> "💎"
                                        },
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(text = gift.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(text = "${gift.starsCost} Stars", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    val success = viewModel.purchaseGift(gift.name, gift.iconType, gift.starsCost)
                                    if (success) {
                                        purchaseResult = "Success! Gilded ${gift.name} added to profile."
                                    } else {
                                        purchaseResult = "Error: Insufficient stars balance. Please purchase more."
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GiftColorPink)
                            ) {
                                Text(text = "Buy Gift", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Owned Gifts list
            Text(
                text = "My Digital Profile Gifts (${userSavedGifts.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (userSavedGifts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You don't own any Star gifts yet. Grab some above!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        userSavedGifts.forEach { giftString ->
                            val parts = giftString.split("~")
                            if (parts.size >= 5) {
                                val id = parts[0]
                                val name = parts[1]
                                val type = parts[2]
                                val cost = parts[3]
                                val ts = parts[4].toLongOrNull() ?: System.currentTimeMillis()
                                val dateStr = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault()).format(Date(ts))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = when(type) {
                                                "Fire" -> "🔥"
                                                "Heart" -> "❤️"
                                                "Bell" -> "🔔"
                                                else -> "💎"
                                            },
                                            fontSize = 18.sp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(text = "Received on $dateStr", fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = StarGold.copy(alpha = 0.15f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(text = cost, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class ShowCaseGift(
    val name: String,
    val iconType: String,
    val starsCost: Int
)
