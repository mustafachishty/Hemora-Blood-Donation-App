package com.example.data.repository

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.local.CachedBloodRequest
import com.example.data.local.CachedNotification
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class LifeDropRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "lifedrop_database"
    ).build()

    private val bloodRequestDao = db.bloodRequestDao()
    private val notificationDao = db.notificationDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    // Current logged-in user state
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // All registered users (Donors)
    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    // Chat messages list
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Donation History
    private val _donationHistory = MutableStateFlow<List<DonationHistory>>(emptyList())
    val donationHistory: StateFlow<List<DonationHistory>> = _donationHistory.asStateFlow()

    // Blood Banks
    private val _bloodBanks = MutableStateFlow<List<BloodBank>>(emptyList())
    val bloodBanks: StateFlow<List<BloodBank>> = _bloodBanks.asStateFlow()

    // Room Database Reactive Flows for requests and notifications
    val activeRequestsFlow: Flow<List<BloodRequest>> = bloodRequestDao.getAllRequests().map { list ->
        list.map { cached ->
            BloodRequest(
                id = cached.id,
                patientName = cached.patientName,
                bloodGroup = cached.bloodGroup,
                unitsRequired = cached.unitsRequired,
                hospitalName = cached.hospitalName,
                hospitalLat = cached.hospitalLat,
                hospitalLng = cached.hospitalLng,
                contactNumber = cached.contactNumber,
                urgencyLevel = cached.urgencyLevel,
                notes = cached.notes,
                timestamp = cached.timestamp,
                expiresAt = cached.expiresAt,
                requesterId = cached.requesterId,
                requesterName = cached.requesterName,
                isFulfilled = cached.isFulfilled
            )
        }
    }

    val notificationsFlow: Flow<List<AppNotification>> = notificationDao.getAllNotifications().map { list ->
        list.map { cached ->
            AppNotification(
                id = cached.id,
                title = cached.title,
                message = cached.message,
                timestamp = cached.timestamp,
                isRead = cached.isRead,
                type = cached.type
            )
        }
    }

    init {
        // Pre-populate mock users, blood banks, and default requests on first initialization
        initializeMockData()
    }

    private fun initializeMockData() {
        scope.launch {
            // Initial mock blood banks
            _bloodBanks.value = listOf(
                BloodBank("bb_1", "Red Cross Blood Center", "401 N Wabash Ave, Chicago, IL", "312-555-0192", 1.2, true, 41.8895, -87.6263),
                BloodBank("bb_2", "Mercy Hospital Blood Bank", "2525 S Michigan Ave, Chicago, IL", "312-555-0143", 3.5, true, 41.8465, -87.6231),
                BloodBank("bb_3", "Northwestern Memorial Blood Depot", "251 E Huron St, Chicago, IL", "312-555-0111", 1.8, true, 41.8943, -87.6212),
                BloodBank("bb_4", "University of Chicago Medicine Center", "5841 S Maryland Ave, Chicago, IL", "773-555-0105", 8.2, false, 41.7891, -87.6048)
            )

            // Initial mock users (donors)
            _allUsers.value = listOf(
                User("user_1", "Sarah Jenkins", "O+", 28, "Female", "Chicago", "", "2026-02-15", false, false, true, 41.8781, -87.6298, false, 250, listOf("First Donor", "Life Saver")),
                User("user_2", "David Miller", "A-", 34, "Male", "Chicago", "", "2025-11-10", false, false, true, 41.8833, -87.6321, true, 500, listOf("First Donor", "Life Saver", "Hero")),
                User("user_3", "Marcus Thompson", "B+", 41, "Male", "Chicago", "", "2026-05-20", false, false, false, 41.8675, -87.6199, false, 80, listOf("First Donor")),
                User("user_4", "Elena Rostova", "AB-", 25, "Female", "Chicago", "", "", false, false, true, 41.8912, -87.6085, false, 50, emptyList()),
                User("user_5", "Michael Chang", "O-", 31, "Male", "Chicago", "", "2026-03-01", false, false, true, 41.8523, -87.6512, false, 750, listOf("First Donor", "Life Saver", "Hero", "Legend")),
                User("user_6", "Aria Vance", "AB+", 29, "Female", "Chicago", "", "2026-01-20", false, false, true, 41.9015, -87.6433, false, 120, listOf("First Donor"))
            )

            // Initial mock requests to be cached
            val initialRequests = listOf(
                CachedBloodRequest(
                    id = "req_1",
                    patientName = "Emily Watson",
                    bloodGroup = "A-",
                    unitsRequired = 3,
                    hospitalName = "Northwestern Memorial Hospital",
                    hospitalLat = 41.8943,
                    hospitalLng = -87.6212,
                    contactNumber = "312-555-0988",
                    urgencyLevel = "Critical",
                    notes = "Scheduled cardiovascular surgery. Immediate match needed.",
                    timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000), // 2 hours ago
                    expiresAt = System.currentTimeMillis() + (22 * 60 * 60 * 1000),
                    requesterId = "user_3",
                    requesterName = "Marcus Thompson",
                    isFulfilled = false
                ),
                CachedBloodRequest(
                    id = "req_2",
                    patientName = "Robert Chen",
                    bloodGroup = "O-",
                    unitsRequired = 2,
                    hospitalName = "Mercy Hospital & Medical Center",
                    hospitalLat = 41.8465,
                    hospitalLng = -87.6231,
                    contactNumber = "312-555-0744",
                    urgencyLevel = "Urgent",
                    notes = "Severe anemia crisis. Matching donor requested as soon as possible.",
                    timestamp = System.currentTimeMillis() - (6 * 60 * 60 * 1000), // 6 hours ago
                    expiresAt = System.currentTimeMillis() + (18 * 60 * 60 * 1000),
                    requesterId = "user_1",
                    requesterName = "Sarah Jenkins",
                    isFulfilled = false
                )
            )
            bloodRequestDao.clearAll()
            bloodRequestDao.insertAllRequests(initialRequests)

            // Initial notification
            val initialNotifications = listOf(
                CachedNotification(
                    id = "not_1",
                    title = "🚨 Emergency Request",
                    message = "Critical: A- Blood required urgently at Northwestern Memorial Hospital.",
                    timestamp = System.currentTimeMillis() - (1 * 60 * 60 * 1000),
                    isRead = false,
                    type = "emergency"
                ),
                CachedNotification(
                    id = "not_2",
                    title = "🏆 Point Achievement",
                    message = "Congratulations! You earned 50 points for completing your donor registration.",
                    timestamp = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
                    isRead = true,
                    type = "eligibility"
                )
            )
            for (n in initialNotifications) {
                notificationDao.insertNotification(n)
            }

            // Initial Chat Messages
            _chatMessages.value = listOf(
                ChatMessage(
                    id = "chat_msg_1",
                    requestId = "req_1",
                    senderId = "user_5", // Michael (O-) responding
                    receiverId = "user_3", // Marcus (requester)
                    text = "Hello Marcus, I received the notification for Emily. I can donate! I have O- which is compatible with A- in emergencies.",
                    timestamp = System.currentTimeMillis() - (45 * 60 * 1000)
                ),
                ChatMessage(
                    id = "chat_msg_2",
                    requestId = "req_1",
                    senderId = "user_3",
                    receiverId = "user_5",
                    text = "Thank you so much Michael! Yes, please. We are at Northwestern, 3rd floor ICU. Can you make it today?",
                    timestamp = System.currentTimeMillis() - (40 * 60 * 1000)
                ),
                ChatMessage(
                    id = "chat_msg_3",
                    requestId = "req_1",
                    senderId = "user_5",
                    receiverId = "user_3",
                    text = "Yes, I am leaving my office now. I'm on my way!",
                    timestamp = System.currentTimeMillis() - (38 * 60 * 1000)
                )
            )

            // Pre-populate self donation history
            _donationHistory.value = listOf(
                DonationHistory("dh_1", "2025-12-10", "Theresa May", "St. Jude Hospital", "O+", 1),
                DonationHistory("dh_2", "2026-03-15", "Lucas Vance", "Chicago General Hospital", "O+", 1)
            )
        }
    }

    // Authenticate / Login simulation
    fun loginWithPhone(phone: String, otp: String): Boolean {
        if (otp == "123456" || otp.length == 6) { // allow standard bypass
            val existing = _allUsers.value.find { it.fullName.contains("Jenkins") } // mock existing Sarah
            val user = User(
                id = "user_self",
                fullName = existing?.fullName ?: "Sarah Jenkins",
                bloodGroup = "O+",
                age = 26,
                gender = "Female",
                city = "Chicago",
                profilePhoto = "https://images.unsplash.com/photo-1494790108377-be9c29b29330",
                lastDonationDate = "2026-03-15", // eligible as of today (June 23, 2026 - >90 days)
                hasDiabetes = false,
                hasRecentSurgery = false,
                isAvailable = true,
                locationLat = 41.8781,
                locationLng = -87.6298,
                showApproximateLocationOnly = false,
                points = 150,
                badges = listOf("First Donor"),
                isBanned = false
            )
            _currentUser.value = user
            return true
        }
        return false
    }

    fun loginWithGoogle(email: String): Boolean {
        val user = User(
            id = "user_self",
            fullName = "Alex Mercer",
            bloodGroup = "AB-",
            age = 29,
            gender = "Male",
            city = "Chicago",
            profilePhoto = "",
            lastDonationDate = "",
            hasDiabetes = false,
            hasRecentSurgery = false,
            isAvailable = true,
            locationLat = 41.8781,
            locationLng = -87.6298,
            showApproximateLocationOnly = false,
            points = 50, // bonus profile points
            badges = emptyList(),
            isBanned = false
        )
        _currentUser.value = user
        return true
    }

    fun logout() {
        _currentUser.value = null
    }

    // Profile updates
    fun updateProfile(user: User) {
        _currentUser.value = user
        // Sync in all users list
        val updatedList = _allUsers.value.filter { it.id != user.id }.toMutableList()
        updatedList.add(user)
        _allUsers.value = updatedList
    }

    // Toggle user availability
    fun toggleAvailability(isAvailable: Boolean) {
        val current = _currentUser.value ?: return
        val updated = current.copy(isAvailable = isAvailable)
        updateProfile(updated)
    }

    // Blood Request actions
    fun postBloodRequest(request: BloodRequest) {
        scope.launch {
            val cached = CachedBloodRequest(
                id = request.id.ifEmpty { UUID.randomUUID().toString() },
                patientName = request.patientName,
                bloodGroup = request.bloodGroup,
                unitsRequired = request.unitsRequired,
                hospitalName = request.hospitalName,
                hospitalLat = request.hospitalLat,
                hospitalLng = request.hospitalLng,
                contactNumber = request.contactNumber,
                urgencyLevel = request.urgencyLevel,
                notes = request.notes,
                timestamp = request.timestamp,
                expiresAt = request.expiresAt,
                requesterId = request.requesterId,
                requesterName = request.requesterName,
                isFulfilled = request.isFulfilled
            )
            bloodRequestDao.insertRequest(cached)

            // Trigger immediate local broadcast notification if matches blood type or is emergency
            val notificationTitle = when (request.urgencyLevel) {
                "Critical" -> "🚨 CRITICAL BLOOD REQUEST"
                "Urgent" -> "⚠️ Urgent Blood Need"
                else -> "🩸 Blood Request"
            }
            val msg = "${request.unitsRequired} units of ${request.bloodGroup} needed at ${request.hospitalName} for ${request.patientName}."
            val notif = CachedNotification(
                id = UUID.randomUUID().toString(),
                title = notificationTitle,
                message = msg,
                timestamp = System.currentTimeMillis(),
                isRead = false,
                type = "blood_request"
            )
            notificationDao.insertNotification(notif)
        }
    }

    fun fulfillRequest(requestId: String) {
        scope.launch {
            bloodRequestDao.updateFulfillment(requestId, true)
            // award user with points
            val current = _currentUser.value
            if (current != null) {
                val newPoints = current.points + 100
                val badges = current.badges.toMutableList()
                if (!badges.contains("First Donor")) {
                    badges.add("First Donor")
                }
                // Check if Life Saver badge earned (assume 5 donations total)
                val donationCount = _donationHistory.value.size + 1
                if (donationCount >= 5 && !badges.contains("Life Saver")) {
                    badges.add("Life Saver")
                }
                if (donationCount >= 10 && !badges.contains("Hero")) {
                    badges.add("Hero")
                }
                
                val updatedHistory = _donationHistory.value.toMutableList()
                updatedHistory.add(0, DonationHistory(
                    id = UUID.randomUUID().toString(),
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    recipientName = "A grateful patient",
                    hospitalName = "Community General",
                    bloodGroup = current.bloodGroup,
                    units = 1
                ))
                _donationHistory.value = updatedHistory

                val updatedUser = current.copy(
                    points = newPoints,
                    badges = badges,
                    lastDonationDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
                updateProfile(updatedUser)

                // Add nice notification
                notificationDao.insertNotification(CachedNotification(
                    id = UUID.randomUUID().toString(),
                    title = "🎉 Donation Completed!",
                    message = "Thank you for saving a life today! You earned +100 Points & the 'First Donor' award.",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    type = "eligibility"
                ))
            }
        }
    }

    fun respondToRequest(requestId: String, donorId: String) {
        scope.launch {
            // Add chat conversation starting point
            val donorName = _allUsers.value.find { it.id == donorId }?.fullName ?: "A helpful donor"
            val requesterName = _currentUser.value?.fullName ?: "Patient"

            val initialMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                requestId = requestId,
                senderId = donorId,
                receiverId = _currentUser.value?.id ?: "requester_id",
                text = "Hello! I saw your urgent request for blood. I am available and heading your way.",
                timestamp = System.currentTimeMillis()
            )
            val updatedChats = _chatMessages.value.toMutableList()
            updatedChats.add(initialMsg)
            _chatMessages.value = updatedChats

            notificationDao.insertNotification(CachedNotification(
                id = UUID.randomUUID().toString(),
                title = "❤️ Donor Responded!",
                message = "$donorName has volunteered to donate blood for your request.",
                timestamp = System.currentTimeMillis(),
                isRead = false,
                type = "chat"
            ))
        }
    }

    // Chat functionality
    fun sendChatMessage(msg: ChatMessage) {
        val updated = _chatMessages.value.toMutableList()
        updated.add(msg)
        _chatMessages.value = updated

        // Simulate a rapid automated reply in the prototype for rich interactivity
        if (msg.senderId == "user_self") {
            scope.launch {
                kotlinx.coroutines.delay(2000)
                val responseText = when {
                    msg.text.lowercase().contains("way") -> "Perfect! Thank you so much. I'm waiting in the hospital lobby."
                    msg.text.lowercase().contains("where") -> "We are at Northwestern Memorial Hospital, 3rd Floor, Room 304."
                    msg.text.lowercase().contains("number") -> "My contact is 312-555-0988, please call me when you reach."
                    else -> "Thank you! Let me know when you arrive."
                }
                val autoReply = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    requestId = msg.requestId,
                    senderId = msg.receiverId,
                    receiverId = msg.senderId,
                    text = responseText,
                    timestamp = System.currentTimeMillis()
                )
                val list = _chatMessages.value.toMutableList()
                list.add(autoReply)
                _chatMessages.value = list

                notificationDao.insertNotification(CachedNotification(
                    id = UUID.randomUUID().toString(),
                    title = "💬 New Message",
                    message = responseText,
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    type = "chat"
                ))
            }
        }
    }

    fun deleteChatsForRequest(requestId: String) {
        _chatMessages.value = _chatMessages.value.filter { it.requestId != requestId }
    }

    fun markNotificationAsRead(id: String) {
        scope.launch {
            notificationDao.markAsRead(id)
        }
    }

    // Admin commands
    fun banUser(userId: String, isBanned: Boolean) {
        val list = _allUsers.value.map {
            if (it.id == userId) it.copy(isBanned = isBanned) else it
        }
        _allUsers.value = list
        if (_currentUser.value?.id == userId) {
            _currentUser.value = _currentUser.value?.copy(isBanned = isBanned)
        }
    }

    fun broadcastEmergencyNotification(title: String, msg: String) {
        scope.launch {
            val cachedNotif = CachedNotification(
                id = UUID.randomUUID().toString(),
                title = "🚨 EMERGENCY BROADCAST: $title",
                message = msg,
                timestamp = System.currentTimeMillis(),
                isRead = false,
                type = "emergency"
            )
            notificationDao.insertNotification(cachedNotif)
        }
    }

    fun getLeaderboard(): List<LeaderboardEntry> {
        val currentSelf = _currentUser.value
        val donors = _allUsers.value.toMutableList()
        if (currentSelf != null && donors.none { it.id == currentSelf.id }) {
            donors.add(currentSelf)
        }

        return donors.sortedByDescending { it.points }.mapIndexed { index, user ->
            val donationCount = if (user.id == "user_self") _donationHistory.value.size else (user.points / 100)
            LeaderboardEntry(
                userId = user.id,
                name = user.fullName,
                bloodGroup = user.bloodGroup,
                city = user.city,
                points = user.points,
                donationCount = donationCount,
                rank = index + 1
            )
        }
    }
}
