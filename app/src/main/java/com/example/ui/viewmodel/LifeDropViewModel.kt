package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiRepository
import com.example.data.model.*
import com.example.data.repository.LifeDropRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object Login : Screen()
    object ProfileSetup : Screen()
    object Home : Screen()
    object MapScreen : Screen()
    object PostRequest : Screen()
    data class RequestDetail(val requestId: String) : Screen()
    object ChatsList : Screen()
    data class ChatRoom(val requestId: String, val contactId: String, val contactName: String) : Screen()
    data class LiveLocationSharing(val requestId: String, val contactId: String, val contactName: String) : Screen()
    object Notifications : Screen()
    object BloodBankDirectory : Screen()
    object EligibilityChecker : Screen()
    object DonationHistoryList : Screen()
    object LeaderboardScreen : Screen()
    object AchievementsScreen : Screen()
    object SettingsScreen : Screen()
    object EditProfileScreen : Screen()
    object AdminLoginScreen : Screen()
    object AdminDashboardScreen : Screen()
}

class LifeDropViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LifeDropRepository(application)
    private val geminiRepository = GeminiRepository()

    // Screen State Management (Backstack)
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenHistory = mutableListOf<Screen>()

    // Expose flows from repository
    val currentUser: StateFlow<User?> = repository.currentUser
    val allUsers: StateFlow<List<User>> = repository.allUsers
    val activeRequests: StateFlow<List<BloodRequest>> = repository.activeRequestsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    val donationHistory: StateFlow<List<DonationHistory>> = repository.donationHistory
    val bloodBanks: StateFlow<List<BloodBank>> = repository.bloodBanks
    val notifications: StateFlow<List<AppNotification>> = repository.notificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Gemini Grounding States
    private val _geminiResponse = MutableStateFlow("")
    val geminiResponse: StateFlow<String> = _geminiResponse.asStateFlow()

    private val _geminiLoading = MutableStateFlow(false)
    val geminiLoading: StateFlow<Boolean> = _geminiLoading.asStateFlow()

    // Filters and Search States
    private val _distanceFilter = MutableStateFlow(25) // default 25km
    val distanceFilter: StateFlow<Int> = _distanceFilter.asStateFlow()

    private val _bloodGroupFilter = MutableStateFlow("All")
    val bloodGroupFilter: StateFlow<String> = _bloodGroupFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // OTP / Phone Input Simulation
    private val _phoneInput = MutableStateFlow("")
    val phoneInput: StateFlow<String> = _phoneInput.asStateFlow()

    private val _otpInput = MutableStateFlow("")
    val otpInput: StateFlow<String> = _otpInput.asStateFlow()

    // Navigation Methods
    fun navigateTo(screen: Screen) {
        screenHistory.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (screenHistory.isNotEmpty()) {
            val prev = screenHistory.removeAt(screenHistory.size - 1)
            _currentScreen.value = prev
        } else {
            // default fallback to Home if possible or close
            _currentScreen.value = Screen.Home
        }
    }

    fun clearHistoryAndNavigateTo(screen: Screen) {
        screenHistory.clear()
        _currentScreen.value = screen
    }

    // Setters
    fun setPhoneInput(phone: String) {
        _phoneInput.value = phone
    }

    fun setOtpInput(otp: String) {
        _otpInput.value = otp
    }

    fun setDistanceFilter(dist: Int) {
        _distanceFilter.value = dist
    }

    fun setBloodGroupFilter(group: String) {
        _bloodGroupFilter.value = group
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Auth Modules
    fun submitPhoneLogin(): Boolean {
        val success = repository.loginWithPhone(_phoneInput.value, _otpInput.value)
        if (success) {
            val user = currentUser.value
            if (user != null && user.fullName.isEmpty()) {
                navigateTo(Screen.ProfileSetup)
            } else {
                clearHistoryAndNavigateTo(Screen.Home)
            }
        }
        return success
    }

    fun submitGoogleLogin(email: String) {
        repository.loginWithGoogle(email)
        navigateTo(Screen.ProfileSetup)
    }

    fun setupProfile(
        name: String,
        bloodGroup: String,
        age: Int,
        gender: String,
        city: String,
        hasDiabetes: Boolean,
        hasRecentSurgery: Boolean
    ) {
        val user = User(
            id = "user_self",
            fullName = name,
            bloodGroup = bloodGroup,
            age = age,
            gender = gender,
            city = city,
            hasDiabetes = hasDiabetes,
            hasRecentSurgery = hasRecentSurgery,
            lastDonationDate = "",
            isAvailable = true,
            locationLat = 41.8781,
            locationLng = -87.6298,
            points = 50, // +50 points for profile completion
            badges = listOf("First Profile")
        )
        repository.updateProfile(user)
        clearHistoryAndNavigateTo(Screen.Home)
    }

    fun editProfile(updated: User) {
        repository.updateProfile(updated)
        navigateBack()
    }

    fun logout() {
        repository.logout()
        clearHistoryAndNavigateTo(Screen.Login)
    }

    fun toggleAvailability(isAvailable: Boolean) {
        repository.toggleAvailability(isAvailable)
    }

    fun addPoints(amount: Int) {
        val user = currentUser.value ?: return
        val updated = user.copy(points = user.points + amount)
        repository.updateProfile(updated)
    }

    // Blood Request Functions
    fun postRequest(
        patientName: String,
        bloodGroup: String,
        units: Int,
        hospital: String,
        contact: String,
        urgency: String,
        notes: String
    ) {
        val user = currentUser.value ?: return
        val req = BloodRequest(
            id = UUID.randomUUID().toString(),
            patientName = patientName,
            bloodGroup = bloodGroup,
            unitsRequired = units,
            hospitalName = hospital,
            contactNumber = contact,
            urgencyLevel = urgency,
            notes = notes,
            timestamp = System.currentTimeMillis(),
            expiresAt = System.currentTimeMillis() + (24 * 60 * 60 * 1000),
            requesterId = user.id,
            requesterName = user.fullName
        )
        repository.postBloodRequest(req)
        navigateBack()
    }

    fun fulfillRequest(requestId: String) {
        repository.fulfillRequest(requestId)
        navigateBack()
    }

    fun respondToRequest(requestId: String, donorId: String) {
        repository.respondToRequest(requestId, donorId)
        val request = activeRequests.value.find { it.id == requestId } ?: return
        navigateTo(Screen.ChatRoom(requestId, request.requesterId, request.requesterName))
    }

    // Chat Functions
    fun sendChatMessage(requestId: String, contactId: String, text: String) {
        val user = currentUser.value ?: return
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            requestId = requestId,
            senderId = user.id,
            receiverId = contactId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        repository.sendChatMessage(msg)
    }

    fun sendLocationMessage(requestId: String, contactId: String, lat: Double, lng: Double) {
        val user = currentUser.value ?: return
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            requestId = requestId,
            senderId = user.id,
            receiverId = contactId,
            text = "📍 Live Location Shared",
            locationShareLat = lat,
            locationShareLng = lng,
            timestamp = System.currentTimeMillis()
        )
        repository.sendChatMessage(msg)
    }

    fun markNotificationAsRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    // Gemini Maps Grounding and Search Helper
    fun askGeminiAssistant(prompt: String) {
        viewModelScope.launch {
            _geminiLoading.value = true
            _geminiResponse.value = ""
            val answer = geminiRepository.getMapsGroundedResponse(prompt)
            _geminiResponse.value = answer
            _geminiLoading.value = false
        }
    }

    fun getLeaderboard(): List<LeaderboardEntry> {
        return repository.getLeaderboard()
    }

    // Admin dashboard metrics
    fun getAdminStats(): Map<String, Int> {
        val users = allUsers.value
        val reqs = activeRequests.value
        return mapOf(
            "TotalDonors" to users.size,
            "ActiveRequests" to reqs.count { !it.isFulfilled },
            "FulfilledToday" to reqs.count { it.isFulfilled },
            "NewRegistrations" to users.size + 1
        )
    }

    fun banUser(userId: String, isBanned: Boolean) {
        repository.banUser(userId, isBanned)
    }

    fun broadcastEmergency(title: String, msg: String) {
        repository.broadcastEmergencyNotification(title, msg)
        navigateBack()
    }
}
