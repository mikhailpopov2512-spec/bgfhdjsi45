package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChatEntity
import com.example.data.ChatRepository
import com.example.data.MessageEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = ChatRepository(database.chatDao())

    // UI Configuration States
    private val prefs = application.getSharedPreferences("telet_prefs", android.content.Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUserPhone = MutableStateFlow("+7 (999) 123-45-67")
    val currentUserPhone: StateFlow<String> = _currentUserPhone.asStateFlow()

    private val _currentUserName = MutableStateFlow("Telet User")
    val currentUserName: StateFlow<String> = _currentUserName.asStateFlow()

    private val _currentUserIsCreator = MutableStateFlow(false)
    val currentUserIsCreator: StateFlow<Boolean> = _currentUserIsCreator.asStateFlow()

    private val _currentUserIsVerified = MutableStateFlow(false)
    val currentUserIsVerified: StateFlow<Boolean> = _currentUserIsVerified.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true) // Default to elegant Midnight Theme
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _drawerOpen = MutableStateFlow(false)
    val drawerOpen: StateFlow<Boolean> = _drawerOpen.asStateFlow()

    private val _showConfigScreen = MutableStateFlow(false)
    val showConfigScreen: StateFlow<Boolean> = _showConfigScreen.asStateFlow()

    private val _currentTab = MutableStateFlow("All") // All, Personal, Groups, Bots
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeChatId = MutableStateFlow<String?>(null)
    val activeChatId: StateFlow<String?> = _activeChatId.asStateFlow()

    // Active screen section: "premium", "stars", "business", "gifts" or null
    private val _activeSection = MutableStateFlow<String?>(null)
    val activeSection: StateFlow<String?> = _activeSection.asStateFlow()

    private val _starsBalance = MutableStateFlow(250)
    val starsBalance: StateFlow<Int> = _starsBalance.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _isBusiness = MutableStateFlow(false)
    val isBusiness: StateFlow<Boolean> = _isBusiness.asStateFlow()

    private val _businessGreeting = MutableStateFlow("Привет! Спасибо за обращение. Я свяжусь с вами в ближайшее время.")
    val businessGreeting: StateFlow<String> = _businessGreeting.asStateFlow()

    private val _businessAddress = MutableStateFlow("Кремлевская набережная, Москва")
    val businessAddress: StateFlow<String> = _businessAddress.asStateFlow()

    // List of active sent/received gifts (stored as string representation "ID~GiftName~Type~Cost~PurchasedTime")
    private val _giftsList = MutableStateFlow<List<String>>(emptyList())
    val giftsList: StateFlow<List<String>> = _giftsList.asStateFlow()

    // Server Diagnostics Status Simulation
    private val _pingStatus = MutableStateFlow("Idle") // Idle, Pinging, Success, Error
    val pingStatus: StateFlow<String> = _pingStatus.asStateFlow()

    private val _typingChatId = MutableStateFlow<String?>(null)
    val typingChatId: StateFlow<String?> = _typingChatId.asStateFlow()

    init {
        val savedLoggedIn = prefs.getBoolean("is_logged_in", false)
        _isLoggedIn.value = savedLoggedIn
        _currentUserPhone.value = prefs.getString("user_phone", "+7 (999) 123-45-67") ?: "+7 (999) 123-45-67"
        _currentUserName.value = prefs.getString("user_name", "Telet User") ?: "Telet User"
        _currentUserIsCreator.value = prefs.getBoolean("user_is_creator", false)
        _currentUserIsVerified.value = prefs.getBoolean("user_is_verified", false)

        _starsBalance.value = prefs.getInt("stars_balance", 250)
        _isPremium.value = prefs.getBoolean("is_premium", false) || _currentUserIsCreator.value
        _isBusiness.value = prefs.getBoolean("is_business", false)
        _businessGreeting.value = prefs.getString("business_greeting", "Привет! Спасибо за обращение. Я свяжусь с вами в ближайшее время.") ?: ""
        _businessAddress.value = prefs.getString("business_address", "Кремлевская набережная, Москва") ?: ""

        // Load premium static or custom saved gifts
        val savedGiftsString = prefs.getString("saved_gifts", "") ?: ""
        if (savedGiftsString.isNotEmpty()) {
            _giftsList.value = savedGiftsString.split("##")
        } else {
            // Default pre-populated showcase gifts for the user
            _giftsList.value = listOf(
                "gift1~Red Rose~Rose~150~1717336400000",
                "gift2~Diamond Ring~Diamond~200~1717337400000"
            )
        }

        // Pre-populate database with Telegram channels/bots on launch
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }
    }

    // Filter and search chats dynamically in a reactive StateFlow
    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredChats: StateFlow<List<ChatEntity>> = combine(
        repository.allChats,
        _currentTab,
        _searchQuery
    ) { chats, tab, query ->
        var list = chats

        // Filter by tab
        list = when (tab) {
            "Personal" -> list.filter { !it.isGroup && !it.isChannel && !it.isBot }
            "Groups" -> list.filter { it.isGroup }
            "Bots" -> list.filter { it.isBot }
            else -> list // "All"
        }

        // Filter by search query
        if (query.isNotEmpty()) {
            list = list.filter { it.title.contains(query, ignoreCase = true) || it.lastMessageText.contains(query, ignoreCase = true) }
        }

        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // FlatMap the active messages whenever activeChatId changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val activeMessages: StateFlow<List<MessageEntity>> = _activeChatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                repository.getMessagesForChat(chatId)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setDrawerOpen(open: Boolean) {
        _drawerOpen.value = open
    }

    fun setShowConfigScreen(show: Boolean) {
        _showConfigScreen.value = show
        if (show) {
            _drawerOpen.value = false
            _activeChatId.value = null
        }
    }

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectChat(chatId: String?) {
        _activeChatId.value = chatId
        if (chatId != null) {
            _showConfigScreen.value = false
            _drawerOpen.value = false
            viewModelScope.launch {
                repository.clearUnread(chatId)
            }
        }
    }

    fun sendMessage(chatId: String, text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            // Send user message
            repository.sendMessage(chatId, _currentUserName.value, text)

            // Trigger simulated typing indicator
            _typingChatId.value = chatId

            // Generate automated agent response
            repository.getAutomatedResponse(chatId, text)

            // Clear typing indicator
            if (_typingChatId.value == chatId) {
                _typingChatId.value = null
            }
        }
    }

    fun selectSection(section: String?) {
        _activeSection.value = section
        if (section != null) {
            _showConfigScreen.value = false
            _drawerOpen.value = false
            _activeChatId.value = null
        }
    }

    fun addStars(count: Int) {
        val newBalance = _starsBalance.value + count
        _starsBalance.value = newBalance
        prefs.edit().putInt("stars_balance", newBalance).apply()
    }

    fun spendStars(count: Int): Boolean {
        if (_starsBalance.value >= count) {
            val newBalance = _starsBalance.value - count
            _starsBalance.value = newBalance
            prefs.edit().putInt("stars_balance", newBalance).apply()
            return true
        }
        return false
    }

    fun togglePremium() {
        val nextVal = !_isPremium.value
        _isPremium.value = nextVal
        prefs.edit().putBoolean("is_premium", nextVal).apply()
    }

    fun updateBusinessInfo(enabled: Boolean, greeting: String, address: String) {
        _isBusiness.value = enabled
        _businessGreeting.value = greeting
        _businessAddress.value = address
        prefs.edit().apply {
            putBoolean("is_business", enabled)
            putString("business_greeting", greeting)
            putString("business_address", address)
            apply()
        }
    }

    fun purchaseGift(giftName: String, iconType: String, starsCost: Int): Boolean {
        if (spendStars(starsCost)) {
            val id = "gift_" + System.currentTimeMillis()
            val time = System.currentTimeMillis().toString()
            val newGifts = _giftsList.value.toMutableList()
            newGifts.add("$id~$giftName~$iconType~$starsCost~$time")
            _giftsList.value = newGifts
            
            val serialized = newGifts.joinToString("##")
            prefs.edit().putString("saved_gifts", serialized).apply()
            return true
        }
        return false
    }

    fun performLogin(phone: String, userName: String, isCreator: Boolean, isVerified: Boolean) {
        _isLoggedIn.value = true
        _currentUserPhone.value = phone
        _currentUserName.value = userName
        _currentUserIsCreator.value = isCreator
        _currentUserIsVerified.value = isVerified
        if (isCreator) {
            _isPremium.value = true
        }

        prefs.edit().apply {
            putBoolean("is_logged_in", true)
            putString("user_phone", phone)
            putString("user_name", userName)
            putBoolean("user_is_creator", isCreator)
            putBoolean("user_is_verified", isVerified)
            putBoolean("is_premium", isCreator || prefs.getBoolean("is_premium", false))
            apply()
        }
    }

    fun performLogout() {
        _isLoggedIn.value = false
        _currentUserPhone.value = "+7 (999) 123-45-67"
        _currentUserName.value = "Telet User"
        _currentUserIsCreator.value = false
        _currentUserIsVerified.value = false

        prefs.edit().apply {
            putBoolean("is_logged_in", false)
            putString("user_phone", "+7 (999) 123-45-67")
            putString("user_name", "Telet User")
            putBoolean("user_is_creator", false)
            putBoolean("user_is_verified", false)
            apply()
        }
    }

    fun createNewChat(title: String, isGroup: Boolean) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val freshChat = repository.addNewCustomChat(title, isGroup)
            selectChat(freshChat.id)
        }
    }

    fun pingServer(host: String) {
        viewModelScope.launch {
            _pingStatus.value = "Pinging $host..."
            delay(1200)
            _pingStatus.value = "Success! Responded 14ms (MTProto v2)"
            delay(3000)
            _pingStatus.value = "Idle"
        }
    }
}
