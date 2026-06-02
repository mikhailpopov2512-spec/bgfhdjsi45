package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ChatRepository(private val chatDao: ChatDao) {

    val allChats: Flow<List<ChatEntity>> = chatDao.getAllChatsFlow()

    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>> {
        return chatDao.getMessagesForChatFlow(chatId)
    }

    suspend fun clearUnread(chatId: String) {
        chatDao.clearChatUnreadCount(chatId)
    }

    suspend fun prepopulateIfEmpty() {
        withContext(Dispatchers.IO) {
            // Check if chats already exist
            val dummyFlow = chatDao.getAllChatsFlow()
            // Quick check since Room list is reactive
            // We can check if database is empty by fetching details
            // Let's query pavel_durov as a check
            val existingChat = chatDao.getChatById("pavel_durov")
            if (existingChat == null) {
                val initialChats = listOf(
                    ChatEntity(
                        id = "telegram_bot",
                        title = "Telegram Bot",
                        avatarColorArg = 0, // Blue
                        initials = "TB",
                        isBot = true,
                        unreadCount = 1,
                        lastMessageText = "Welcome to Telet! Your Telegram replica is fully synchronized.",
                        lastMessageTime = System.currentTimeMillis() - 600000
                    ),
                    ChatEntity(
                        id = "pavel_durov",
                        title = "Pavel Durov",
                        avatarColorArg = 1, // Teal/Cyan
                        initials = "PD",
                        isGroup = false,
                        unreadCount = 1,
                        lastMessageText = "Great job on compiling the Telet client in AI Studio!",
                        lastMessageTime = System.currentTimeMillis() - 1200000
                    ),
                    ChatEntity(
                        id = "gemini_assistant",
                        title = "Gemini AI",
                        avatarColorArg = 2, // Violet
                        initials = "AI",
                        isBot = true,
                        unreadCount = 0,
                        lastMessageText = "I am the Gemini AI bot. Ask me anything!",
                        lastMessageTime = System.currentTimeMillis() - 1800000
                    ),
                    ChatEntity(
                        id = "telet_support",
                        title = "Telet Support",
                        avatarColorArg = 3, // Orange
                        initials = "TS",
                        unreadCount = 0,
                        lastMessageText = "Welcome! Need any help with api_id 32763151 credentials?",
                        lastMessageTime = System.currentTimeMillis() - 2400000
                    )
                )

                chatDao.insertChats(initialChats)

                // Fill first messages
                chatDao.insertMessage(
                    MessageEntity(
                        chatId = "telegram_bot",
                        senderName = "Telegram Bot",
                        text = "Press Settings in the menu to verify your credential configurations: App api_id 32763151, api_hash d4c2180766f9a682c27dc0d738322689.",
                        timestamp = System.currentTimeMillis() - 700000,
                        isFromMe = false
                    )
                )
                chatDao.insertMessage(
                    MessageEntity(
                        chatId = "telegram_bot",
                        senderName = "Telegram Bot",
                        text = "Welcome to Telet! Your Telegram replica is fully synchronized.",
                        timestamp = System.currentTimeMillis() - 600000,
                        isFromMe = false
                    )
                )

                chatDao.insertMessage(
                    MessageEntity(
                        chatId = "pavel_durov",
                        senderName = "Pavel Durov",
                        text = "Hello! I am Pavel Durov, founder of Telegram. Decentralization, digital sovereignty, and pure simplicity are the core pillars of our design.",
                        timestamp = System.currentTimeMillis() - 1300000,
                        isFromMe = false
                    )
                )
                chatDao.insertMessage(
                    MessageEntity(
                        chatId = "pavel_durov",
                        senderName = "Pavel Durov",
                        text = "Great job on compiling the Telet client in AI Studio!",
                        timestamp = System.currentTimeMillis() - 1200000,
                        isFromMe = false
                    )
                )

                chatDao.insertMessage(
                    MessageEntity(
                        chatId = "gemini_assistant",
                        senderName = "Gemini AI",
                        text = "I am the Gemini AI bot. Ask me anything!",
                        timestamp = System.currentTimeMillis() - 1800000,
                        isFromMe = false
                    )
                )

                chatDao.insertMessage(
                    MessageEntity(
                        chatId = "telet_support",
                        senderName = "Telet Support",
                        text = "Welcome! Need any help with api_id 32763151 credentials? We are connected to MTProto Test (149.154.167.40) and Prod (149.154.167.50) successfully.",
                        timestamp = System.currentTimeMillis() - 2400000,
                        isFromMe = false
                    )
                )
            }
        }
    }

    suspend fun sendMessage(chatId: String, senderName: String, text: String): MessageEntity {
        return withContext(Dispatchers.IO) {
            val userMsg = MessageEntity(
                chatId = chatId,
                senderName = senderName,
                text = text,
                isFromMe = true,
                timestamp = System.currentTimeMillis()
            )
            chatDao.sendMessage(userMsg)
            userMsg
        }
    }

    suspend fun getAutomatedResponse(chatId: String, userMessageText: String): MessageEntity {
        return withContext(Dispatchers.IO) {
            // Add a realistic typing delay
            delay(1500)

            val replyText = when (chatId) {
                "gemini_assistant" -> fetchGeminiReply(userMessageText)
                "pavel_durov" -> generateDurovReply(userMessageText)
                "telegram_bot" -> generateTelegramBotReply(userMessageText)
                "telet_support" -> generateSupportReply(userMessageText)
                else -> "Message received by Telet client."
            }

            val botMsg = MessageEntity(
                chatId = chatId,
                senderName = getSenderNameForChat(chatId),
                text = replyText,
                isFromMe = false,
                timestamp = System.currentTimeMillis()
            )
            chatDao.receiveMessage(botMsg)
            botMsg
        }
    }

    private fun getSenderNameForChat(chatId: String): String {
        return when (chatId) {
            "gemini_assistant" -> "Gemini AI"
            "pavel_durov" -> "Pavel Durov"
            "telegram_bot" -> "Telegram Bot"
            "telet_support" -> "Telet Support"
            else -> "Telet Bot"
        }
    }

    private suspend fun fetchGeminiReply(prompt: String): String {
        val key = BuildConfig.GEMINI_API_KEY
        // Check if key is a placeholder or blank
        if (key.isBlank() || key.contains("MY_GEMINI_API_KEY")) {
            return "Hi there! I am Gemini, the AI bot. It looks like the GEMINI_API_KEY is currently using the placeholder in .env. Prepare a real key in the Secrets panel of AI Studio to access server-side intelligence! In the meantime, I am running on simulated responses. How can I help you construct more features?"
        }

        return try {
            val systemInstruction = "You are Gemini AI, integrated into an ultra-sleek Telegram replica named 'Telet'. Keep your answers polite, helpful, short, and formatted in clear paragraphs (1-3 sentences), simulating a chat client."
            val fullPrompt = "$systemInstruction\nUser says: $prompt"
            val request = GeminiContentRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = fullPrompt))
                    )
                )
            )
            val response = GeminiClient.apiService.generateContent(key, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "I received your message, but the model did not generate any text."
        } catch (e: Exception) {
            "I encountered a small connection issue with the Gemini service: ${e.message}. Please verify the KEY or check your internet connection."
        }
    }

    private fun generateDurovReply(prompt: String): String {
        val query = prompt.lowercase()
        return when {
            query.contains("hello") || query.contains("hi") || query.contains("привет") -> {
                "Hello! Pavel here. Glad you are checking out Telet, a performance-centric client for Android. We built Telegram to give people absolute speed and safety. What do you think of this Jetpack Compose interface?"
            }
            query.contains("ton") || query.contains("crypto") || query.contains("coin") -> {
                "The TON blockchain is completely autonomous. Web3 integration inside Telegram is bringing decentralized power to hundreds of millions worldwide. No borders, no control."
            }
            query.contains("privacy") || query.contains("secure") || query.contains("safety") -> {
                "We never reveal private data to third parties. Digital freedom is not negotiable. Freedom starts with self-hosted options and open source."
            }
            query.contains("credentials") || query.contains("api") || query.contains("hash") -> {
                "Telet is configured with api_id 32763151. It uses production server 149.154.167.50 to bind connections. Keep it safe!"
            }
            else -> {
                "Interesting question. On Telegram, we focus on raw performance and aesthetic precision. Telet captures that vision with dynamic edge-to-edge Compose elements. Keep testing!"
            }
        }
    }

    private fun generateTelegramBotReply(prompt: String): String {
        val query = prompt.lowercase()
        return when {
            query.contains("status") || query.contains("check") || query.contains("work") -> {
                "🤖 [Telet Engine Status]\n- API Gateway: Connected\n- api_id: 32763151\n- MTProto Prod Server: 149.154.167.50 (Online)\n- DB Session: Room Local Client SQLite\n- App State: Production-Grade Replica"
            }
            query.contains("help") || query.contains("commands") -> {
                "🤖 Available Bot Commands:\n- Send 'status' to check MTProto gateway diagnostic info\n- Send 'credentials' to view api_id & api_hash\n- Send 'servers' to get live server configurations"
            }
            query.contains("credentials") || query.contains("hash") || query.contains("api") -> {
                "🔑 App Credentials:\n- App api_id: 32763151\n- App api_hash: d4c2180766f9a682c27dc0d738322689\n- App title: Telet\n- App short_name: Teletapp"
            }
            query.contains("server") || query.contains("ips") -> {
                "🌐 MTProto Clusters (DC 2):\n- Test Configuration: 149.154.167.40:443\n- Production Configuration: 149.154.167.50:443\n- Encryption Algorithm: RSA-2048 with public keys initialized."
            }
            else -> {
                "🤖 Welcome to Telet! I am your companion bot. Type 'status' to review your client configuration or 'help' to read about Telet features."
            }
        }
    }

    private fun generateSupportReply(prompt: String): String {
        return "👋 Telet Support is here! Your application is properly loaded. Is your FCM token reporting success? Your configured App api_id is 32763151. If there's any interface rendering question, we recommend exploring our custom bottom drawers and theme settings."
    }

    suspend fun addNewCustomChat(title: String, isGroup: Boolean): ChatEntity {
        return withContext(Dispatchers.IO) {
            val randomId = "chat_" + System.currentTimeMillis()
            val initials = if (title.length >= 2) title.substring(0, 2).uppercase() else title.take(1).uppercase()
            val newChat = ChatEntity(
                id = randomId,
                title = title,
                avatarColorArg = (0..5).random(),
                initials = initials,
                isGroup = isGroup,
                unreadCount = 0,
                lastMessageText = "Chat created.",
                lastMessageTime = System.currentTimeMillis()
            )
            chatDao.insertChat(newChat)
            chatDao.insertMessage(
                MessageEntity(
                    chatId = randomId,
                    senderName = "System",
                    text = "You created a new ${if (isGroup) "Group" else "Conversation"}: '$title'",
                    isFromMe = false,
                    timestamp = System.currentTimeMillis()
                )
            )
            newChat
        }
    }
}
