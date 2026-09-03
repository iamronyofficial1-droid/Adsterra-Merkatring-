package com.senapotixchange.data.model

// Adsterra API Connection State
data class AdsterraAccountDto(
    val publisherId: String = "PUB-849201",
    val email: String = "iamronyofficial1@gmail.com",
    val apiKey: String = "",
    val isConnected: Boolean = false,
    val balance: Double = 348.65,
    val payoutMethod: String = "USDT (TRC20)",
    val nextPayoutDate: String = "Sep 16, 2026",
    val totalPlacements: Int = 6,
    val activeSmartLinks: Int = 4,
    val lastSyncedTimestamp: Long = System.currentTimeMillis()
)

enum class AdsterraApiStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

// Adsterra Smart Link & Placement Model
data class AdsterraSmartLinkDto(
    val placementId: String,
    val name: String,
    val url: String,
    val format: String, // Smartlink, Direct Link, Popunder, Social Bar, Native Banner
    val domain: String,
    val status: String, // ACTIVE, PAUSED, PENDING
    val todayRevenue: Double,
    val todayImpressions: Int,
    val todayClicks: Int,
    val currentCpm: Double,
    val isImportedToExchange: Boolean = false,
    val importedCampaignId: String? = null
)

// Placement Detailed Real-Time Stat
data class PlacementRealtimeStat(
    val placementId: String,
    val name: String,
    val format: String,
    val impressions: Int,
    val clicks: Int,
    val ctr: Double,
    val cpm: Double,
    val revenue: Double,
    val topCountry: String
)

// Hourly Revenue Breakdown
data class HourlyStatPoint(
    val hour: String,
    val revenue: Double,
    val impressions: Int,
    val cpm: Double
)

// Adsterra Live Realtime Dashboard Data
data class AdsterraLiveDashboard(
    val account: AdsterraAccountDto,
    val summary: AdsterraSummary,
    val smartLinks: List<AdsterraSmartLinkDto>,
    val placementStats: List<PlacementRealtimeStat>,
    val hourlyStats: List<HourlyStatPoint>,
    val countryStats: List<CountryStat>,
    val dateRange: AdsterraDateRange,
    val isLiveStreaming: Boolean = true
)
