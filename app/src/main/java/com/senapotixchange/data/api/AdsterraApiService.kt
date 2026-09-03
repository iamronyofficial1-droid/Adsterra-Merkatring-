package com.senapotixchange.data.api

import com.senapotixchange.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AdsterraApiService {

    suspend fun connectWithApiKey(apiKey: String): Result<AdsterraAccountDto> = withContext(Dispatchers.IO) {
        delay(800) // Simulate network validation
        if (apiKey.trim().length < 8) {
            return@withContext Result.failure(IllegalArgumentException("Invalid API key format. Adsterra API keys typically contain 32-64 characters."))
        }

        val account = AdsterraAccountDto(
            publisherId = "PUB-" + (apiKey.hashCode().toLong() and 0xFFFFFF).toString().padStart(6, '7'),
            email = if (apiKey.contains("test") || apiKey.length < 15) "publisher@senapoti.io" else "publisher.vip@adsterra-hub.net",
            apiKey = apiKey.trim(),
            isConnected = true,
            balance = 482.90,
            payoutMethod = "USDT (TRC20)",
            nextPayoutDate = "Sep 16, 2026",
            totalPlacements = 6,
            activeSmartLinks = 5,
            lastSyncedTimestamp = System.currentTimeMillis()
        )
        Result.success(account)
    }

    suspend fun fetchAccountSmartLinks(apiKey: String): List<AdsterraSmartLinkDto> = withContext(Dispatchers.IO) {
        delay(400)
        listOf(
            AdsterraSmartLinkDto(
                placementId = "plc_894101",
                name = "DirectLink US/Tier1 High-CPM",
                url = "https://beta.publishers.adsterra.com/direct/US-Tier1-Exclusive",
                format = "Direct Link",
                domain = "publishers.adsterra.com",
                status = "ACTIVE",
                todayRevenue = 28.45,
                todayImpressions = 3280,
                todayClicks = 295,
                currentCpm = 8.67,
                isImportedToExchange = false
            ),
            AdsterraSmartLinkDto(
                placementId = "plc_894102",
                name = "Global Smartlink Tech & Gaming",
                url = "https://beta.publishers.adsterra.com/smartlink/gaming-global-v2",
                format = "Smartlink",
                domain = "publishers.adsterra.com",
                status = "ACTIVE",
                todayRevenue = 18.20,
                todayImpressions = 2650,
                todayClicks = 184,
                currentCpm = 6.87,
                isImportedToExchange = false
            ),
            AdsterraSmartLinkDto(
                placementId = "plc_894103",
                name = "Fintech & Crypto Smartlink Multi-Geo",
                url = "https://publishers.adsterra.com/smartlink/fintech-crypto-hub",
                format = "Smartlink",
                domain = "publishers.adsterra.com",
                status = "ACTIVE",
                todayRevenue = 34.60,
                todayImpressions = 2980,
                todayClicks = 310,
                currentCpm = 11.61,
                isImportedToExchange = false
            ),
            AdsterraSmartLinkDto(
                placementId = "plc_894104",
                name = "Social Bar Clean Traffic Direct",
                url = "https://publishers.adsterra.com/direct/social-bar-clean",
                format = "Social Bar",
                domain = "publishers.adsterra.com",
                status = "ACTIVE",
                todayRevenue = 12.80,
                todayImpressions = 2100,
                todayClicks = 142,
                currentCpm = 6.10,
                isImportedToExchange = false
            ),
            AdsterraSmartLinkDto(
                placementId = "plc_894105",
                name = "Popunder Main Website Stream",
                url = "https://publishers.adsterra.com/popunder/stream-portal-v1",
                format = "Popunder",
                domain = "publishers.adsterra.com",
                status = "ACTIVE",
                todayRevenue = 9.40,
                todayImpressions = 1850,
                todayClicks = 95,
                currentCpm = 5.08,
                isImportedToExchange = false
            ),
            AdsterraSmartLinkDto(
                placementId = "plc_894106",
                name = "Native Banner 4:1 Adsterra Unit",
                url = "https://publishers.adsterra.com/native/banner-blog-sidebar",
                format = "Native Banner",
                domain = "publishers.adsterra.com",
                status = "PAUSED",
                todayRevenue = 2.10,
                todayImpressions = 450,
                todayClicks = 28,
                currentCpm = 4.67,
                isImportedToExchange = false
            )
        )
    }

    suspend fun fetchHourlyPerformance(): List<HourlyStatPoint> = withContext(Dispatchers.IO) {
        listOf(
            HourlyStatPoint("00:00", 2.10, 240, 8.75),
            HourlyStatPoint("03:00", 1.80, 210, 8.57),
            HourlyStatPoint("06:00", 3.40, 420, 8.10),
            HourlyStatPoint("09:00", 6.80, 780, 8.72),
            HourlyStatPoint("12:00", 8.90, 1020, 8.73),
            HourlyStatPoint("15:00", 11.20, 1290, 8.68),
            HourlyStatPoint("18:00", 9.40, 1080, 8.70),
            HourlyStatPoint("21:00", 7.10, 810, 8.77)
        )
    }

    suspend fun fetchPlacementBreakdown(): List<PlacementRealtimeStat> = withContext(Dispatchers.IO) {
        listOf(
            PlacementRealtimeStat("plc_894103", "Fintech & Crypto Smartlink", "Smartlink", 2980, 310, 10.4, 11.61, 34.60, "US (64%)"),
            PlacementRealtimeStat("plc_894101", "DirectLink US/Tier1 High-CPM", "Direct Link", 3280, 295, 9.0, 8.67, 28.45, "US (82%)"),
            PlacementRealtimeStat("plc_894102", "Global Smartlink Tech", "Smartlink", 2650, 184, 6.9, 6.87, 18.20, "GB (45%)"),
            PlacementRealtimeStat("plc_894104", "Social Bar Clean Traffic", "Social Bar", 2100, 142, 6.7, 6.10, 12.80, "CA (38%)"),
            PlacementRealtimeStat("plc_894105", "Popunder Main Website", "Popunder", 1850, 95, 5.1, 5.08, 9.40, "DE (51%)")
        )
    }
}
