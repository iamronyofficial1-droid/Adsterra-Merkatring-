package com.senapotixchange.data.model

// User & Profile
data class UserDto(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val credits: Double = 0.0,
    val xp: Int = 0,
    val level: Int = 1,
    val rank: String = "Bronze Publisher",
    val verified: Boolean = false,
    val isVip: Boolean = false,
    val bio: String = "",
    val referralCode: String = "",
    val totalVisits: Int = 0,
    val todayVisits: Int = 0,
    val totalEarnings: Double = 0.0,
    val dailyLimit: Int = 100
)

data class ProfileStatsDto(
    val totalLinks: Int = 0,
    val activeLinks: Int = 0,
    val totalClicks: Int = 0,
    val totalCreditsSpent: Double = 0.0,
    val totalCreditsEarned: Double = 0.0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val rankPosition: Int = 0
)

data class ProfileUpdateRequest(
    val username: String,
    val bio: String,
    val avatarUrl: String? = null
)

// Campaign Links
data class LinkDto(
    val id: String,
    val userId: String,
    val username: String,
    val title: String,
    val url: String,
    val category: String, // General, Adsterra Direct Link, Smartlink, Blog, Tech, Finance
    val creditsPerVisit: Double,
    val dailyLimit: Int,
    val todayVisits: Int = 0,
    val totalVisits: Int = 0,
    val active: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val isAdsterra: Boolean = false,
    val estimatedCpm: Double = 0.0
)

data class CreateLinkRequest(
    val title: String,
    val url: String,
    val category: String,
    val creditsPerVisit: Double,
    val dailyLimit: Int,
    val isAdsterra: Boolean
)

data class UpdateLinkRequest(
    val title: String? = null,
    val active: Boolean? = null,
    val dailyLimit: Int? = null,
    val creditsPerVisit: Double? = null
)

// Analytics & Adsterra Stats
enum class AdsterraDateRange(val label: String) {
    TODAY("Today"),
    YESTERDAY("Yesterday"),
    LAST_7_DAYS("Last 7 Days"),
    LAST_30_DAYS("Last 30 Days"),
    MONTH_TO_DATE("Month to Date")
}

data class RevenueDataPoint(
    val date: String,
    val revenue: Double,
    val impressions: Int,
    val clicks: Int,
    val cpm: Double
)

data class CountryStat(
    val countryCode: String,
    val countryName: String,
    val revenue: Double,
    val impressions: Int,
    val clicks: Int,
    val cpm: Double
)

data class AdsterraSummary(
    val todayRevenue: Double = 0.0,
    val yesterdayRevenue: Double = 0.0,
    val sevenDayRevenue: Double = 0.0,
    val monthRevenue: Double = 0.0,
    val todayCpm: Double = 0.0,
    val averageCpm: Double = 0.0,
    val totalImpressions: Int = 0,
    val totalClicks: Int = 0,
    val ctr: Double = 0.0
)

data class StatsResponse(
    val summary: AdsterraSummary,
    val revenuePoints: List<RevenueDataPoint>,
    val countryStats: List<CountryStat>
)

// Leaderboard
data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val level: Int,
    val score: Double,
    val verified: Boolean = false,
    val badge: String = ""
)

data class AdsterraLeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val revenue: Double,
    val impressions: Int,
    val clicks: Int,
    val verified: Boolean = false
)

// Level & Quests
data class XpTask(
    val id: String,
    val title: String,
    val description: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val xpReward: Int,
    val creditReward: Double,
    val category: String, // Daily, Weekly, Milestone
    val completed: Boolean = false,
    val claimed: Boolean = false
)

data class LevelResponse(
    val currentLevel: Int,
    val currentXp: Int,
    val nextLevelXp: Int,
    val tierName: String,
    val tierMultiplier: Double,
    val tasks: List<XpTask>
)

// Membership & VIP
data class MembershipPlanDto(
    val id: String,
    val name: String,
    val price: Double,
    val durationDays: Int,
    val visitMultiplier: Double,
    val dailyLimitBoost: Int,
    val priorityQueue: Boolean,
    val adFree: Boolean,
    val badge: String,
    val features: List<String>
)

data class MembershipStatusDto(
    val isVip: Boolean = false,
    val currentPlan: String = "Free Pioneer",
    val expiresAt: Long = 0L,
    val daysRemaining: Int = 0,
    val visitMultiplier: Double = 1.0,
    val priorityQueue: Boolean = false
)

// Shop & Credits
data class ProductDto(
    val id: String,
    val name: String,
    val creditsAmount: Double,
    val bonusCredits: Double,
    val priceUsd: Double,
    val popular: Boolean = false,
    val description: String = ""
)

data class PaymentMethodInfo(
    val id: String,
    val name: String,
    val iconRes: String,
    val feePercent: Double = 0.0
)

data class OrderDto(
    val orderId: String,
    val productName: String,
    val amount: Double,
    val priceUsd: Double,
    val paymentMethod: String,
    val timestamp: Long,
    val status: String // COMPLETED, PENDING, FAILED
)

// Referral
data class ReferredUserDto(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val joinedDate: String,
    val visitsCount: Int,
    val commissionEarned: Double,
    val active: Boolean
)

data class ReferralResponse(
    val referralCode: String,
    val referralLink: String,
    val totalReferrals: Int,
    val activeReferrals: Int,
    val totalEarnedCredits: Double,
    val commissionRatePercent: Double,
    val referredUsers: List<ReferredUserDto>
)

// Notifications
data class NotificationDto(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val read: Boolean = false,
    val type: String // REWARD, LEVEL_UP, CAMPAIGN, SYSTEM, REFERRAL
)

data class NotificationPrefsRequest(
    val rewardNotifications: Boolean = true,
    val campaignBudgetAlerts: Boolean = true,
    val referralAlerts: Boolean = true,
    val systemAnnouncements: Boolean = true
)

// Visit Request & Response
data class VisitRequest(
    val linkId: String,
    val durationSeconds: Int = 15
)

data class VisitResponse(
    val success: Boolean,
    val creditsEarned: Double,
    val xpEarned: Int,
    val newBalance: Double,
    val message: String
)

data class ServerVisitStatusDto(
    val active: Boolean,
    val visitsToday: Int,
    val dailyLimit: Int,
    val creditsPerHour: Double
)
