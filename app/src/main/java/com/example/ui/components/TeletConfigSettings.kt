package com.example.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeletConfigSettings(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val pingStatus by viewModel.pingStatus.collectAsState()
    val isCreator by viewModel.currentUserIsCreator.collectAsState()

    val apiId = "32763151"
    val apiHash = "d4c2180766f9a682c27dc0d738322689"
    val appTitle = "Telet"
    val shortName = "Teletapp"

    val testIp = "149.154.167.40:443"
    val prodIp = "149.154.167.50:443"

    val testPublicKey = """
        -----BEGIN RSA PUBLIC KEY-----
        MIIBCgKCAQEAyMedy1aR+sCR3ZSJrtztKTKqigv0/vBfqACJLZtS7QMgCGXJ6XIR
        yy7mx66W0/sOFa7/1mAZTeOIokDP3ShoqF4fVNb6XeqgQfaUHd8wJpDWhcR2OFwv
        plUUI1PLTktZ9uW2WE23b+ixNwJJjGwBDJPQEQFBE+vfmH0JP503wr5INS1poWg/
        j25sIWeyPHYeOrFp/EXaqhISP6G+q2IeTaWTXpwZj4Lzxq5YOpdKCSwHnd6FudwGO4pcCO
        j4WcDuXc2CTHgH8gFTNHp/Y8/SpD0hvn9QIDAQAB
        -----END RSA PUBLIC KEY-----
    """.trimIndent()

    val prodPublicKey = """
        -----BEGIN RSA PUBLIC KEY-----
        MIIBCgKCAQEA6LSzBcc1LGzyr99zNzE0iey+BSa0W622Aa9Bd4ZHLl+TuFQ4lo4g
        5nKaMBwK/BiB9xUfgQ29/2mgIR6Zr9krM7HjuIccZfvDtr+L0GQjae9H0pRB200
        62cECs5HKhT5DZ98K33vmWiLowc621dQuwKwSQKjwf50XYFw42h21P2KXUGyp2y/
        +aEyZ+uVgLLQbRA1dEySDZ2iGrY12Mk5gpYc397aYp438fSJoHIgJ2lgMv5h7WY9
        t6N/byY9Nw9p210g3AoXSL2q/2IJ1WRUhebgAdGVM1V1fkuQ0oEzR7EdpqtQD9Cs
        5+bfo3Nhmcyvk5ftB0wKj9z6bNZ7yxrP8wIDAQAB
        -----END RSA PUBLIC KEY-----
    """.trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App configuration", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("config_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            if (isCreator) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .border(1.5.dp, Color(0xFFE53935).copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE53935).copy(alpha = 0.12f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE53935))
                            )
                            Text(
                                text = "ПАНЕЛЬ УПРАВЛЕНИЯ СОЗДАТЕЛЯ (CREATOR PANEL)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFFE53935),
                                letterSpacing = 1.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Статус: Главный Создатель (Root Creator) 🛡️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "✔️ Все права доступа к MTProto шлюзам активированы.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        
                        Text(
                            text = "✔️ Верифицированный статус Telegram-клиента (Verified badge).",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        
                        Text(
                            text = "✔️ Полные права на конфигурацию, изменение API ID / API Hash.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        
                        Text(
                            text = "✔️ Обход двухфакторной аутентификации SMS по PIN-коду 2580.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Diagnostic status card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "MTProto Connectivity Diagnostic",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Status: $pingStatus",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = { viewModel.pingServer("149.154.167.50") },
                            modifier = Modifier.testTag("ping_prod_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.NetworkCheck, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ping Prod Server", fontSize = 12.sp)
                        }
                    }
                }
            }

            // App api_id config block
            ConfigDisplayLabel(title = "App api_id:")
            ConfigValueBox(value = apiId, onCopy = { clipboardManager.setText(AnnotatedString(apiId)) })
            Spacer(modifier = Modifier.height(2.dp))
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "🔒 Lock",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App api_hash config block
            ConfigDisplayLabel(title = "App api_hash:")
            ConfigValueBox(value = apiHash, onCopy = { clipboardManager.setText(AnnotatedString(apiHash)) })
            Spacer(modifier = Modifier.height(2.dp))
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "🔒 Lock",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App title
            ConfigDisplayLabel(title = "App title:")
            ConfigValueBox(value = appTitle, onCopy = { clipboardManager.setText(AnnotatedString(appTitle)) })

            Spacer(modifier = Modifier.height(16.dp))

            // Short name
            ConfigDisplayLabel(title = "Short name:")
            ConfigValueBox(value = shortName, onCopy = { clipboardManager.setText(AnnotatedString(shortName)) })
            Text(
                text = "alphanumeric, 5-32 characters",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Available MTProto servers",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TEST Config block
            Text(
                text = "Test configuration:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            ConfigIpBox(ip = testIp, region = "DC 2", onCopy = { clipboardManager.setText(AnnotatedString(testIp)) })

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Public keys:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            ConfigKeyBox(keyContent = testPublicKey, onCopy = { clipboardManager.setText(AnnotatedString(testPublicKey)) })

            Spacer(modifier = Modifier.height(24.dp))

            // PROD Config block
            Text(
                text = "Production configuration:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
            ConfigIpBox(ip = prodIp, region = "DC 2", onCopy = { clipboardManager.setText(AnnotatedString(prodIp)) })

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Public keys:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            ConfigKeyBox(keyContent = prodPublicKey, onCopy = { clipboardManager.setText(AnnotatedString(prodPublicKey)) })

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ConfigDisplayLabel(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun ConfigValueBox(value: String, onCopy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(18.dp)
                .clickable(onClick = onCopy)
        )
    }
}

@Composable
fun ConfigIpBox(ip: String, region: String, onCopy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = ip,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = region,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(18.dp)
                .clickable(onClick = onCopy)
        )
    }
}

@Composable
fun ConfigKeyBox(keyContent: String, onCopy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = keyContent,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy Keys",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(18.dp)
                .clickable(onClick = onCopy)
        )
    }
}
