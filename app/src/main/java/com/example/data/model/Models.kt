package com.example.data.model

data class User(
    val id: String = "",
    val fullName: String = "",
    val bloodGroup: String = "",
    val age: Int = 18,
    val gender: String = "",
    val city: String = "",
    val profilePhoto: String = "",
    val lastDonationDate: String = "", // YYYY-MM-DD
    val hasDiabetes: Boolean = false,
    val hasRecentSurgery: Boolean = false,
    val isAvailable: Boolean = true,
    val locationLat: Double = 37.7749,
    val locationLng: Double = -122.4194,
    val showApproximateLocationOnly: Boolean = false,
    val points: Int = 0,
    val badges: List<String> = emptyList(),
    val isBanned: Boolean = false
) {
    fun isEligibleToDonate(): Boolean {
        if (age < 18 || age > 65) return false
        if (hasDiabetes || hasRecentSurgery) return false
        if (lastDonationDate.isEmpty()) return true
        
        // Gap check (90 days)
        try {
            val parts = lastDonationDate.split("-")
            if (parts.size == 3) {
                val year = parts[0].toIntOrNull() ?: return true
                val month = parts[1].toIntOrNull() ?: return true
                val day = parts[2].toIntOrNull() ?: return true
                
                // Simple epoch day approximation
                val lastDateDays = (year * 365) + (month * 30) + day
                val todayDays = (2026 * 365) + (6 * 30) + 23 // Current time: June 2026
                return (todayDays - lastDateDays) >= 90
            }
        } catch (e: Exception) {
            return true
        }
        return true
    }
    
    fun daysUntilEligible(): Int {
        if (lastDonationDate.isEmpty()) return 0
        try {
            val parts = lastDonationDate.split("-")
            if (parts.size == 3) {
                val year = parts[0].toIntOrNull() ?: return 0
                val month = parts[1].toIntOrNull() ?: return 0
                val day = parts[2].toIntOrNull() ?: return 0
                
                val lastDateDays = (year * 365) + (month * 30) + day
                val todayDays = (2026 * 365) + (6 * 30) + 23
                val diff = todayDays - lastDateDays
                return if (diff >= 90) 0 else (90 - diff)
            }
        } catch (e: Exception) {
            return 0
        }
        return 0
    }
}

data class BloodRequest(
    val id: String = "",
    val patientName: String = "",
    val bloodGroup: String = "",
    val unitsRequired: Int = 1,
    val hospitalName: String = "",
    val hospitalLat: Double = 37.7749,
    val hospitalLng: Double = -122.4194,
    val contactNumber: String = "",
    val urgencyLevel: String = "Normal", // Normal / Urgent / Critical
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val expiresAt: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000), // 24h
    val requesterId: String = "",
    val requesterName: String = "",
    val isFulfilled: Boolean = false,
    val respondedDonors: List<String> = emptyList()
)

data class ChatMessage(
    val id: String = "",
    val requestId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val locationShareLat: Double? = null,
    val locationShareLng: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class BloodBank(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val distance: Double = 0.0,
    val isOpen: Boolean = true,
    val lat: Double = 37.7749,
    val lng: Double = -122.4194
)

data class AppNotification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "blood_request" // emergency / blood_request / chat / eligibility
)

data class DonationHistory(
    val id: String = "",
    val date: String = "",
    val recipientName: String = "",
    val hospitalName: String = "",
    val bloodGroup: String = "",
    val units: Int = 1
)

data class LeaderboardEntry(
    val userId: String,
    val name: String,
    val bloodGroup: String,
    val city: String,
    val points: Int,
    val donationCount: Int,
    val rank: Int
)
