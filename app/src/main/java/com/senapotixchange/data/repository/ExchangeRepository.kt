package com.senapotixchange.data.repository

import com.senapotixchange.data.api.AdsterraApiService
import com.senapotixchange.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ExchangeRepository {

    private val adsterraApiService = AdsterraApiService()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Current User State
    private val _currentUser = MutableStateFlow(
        UserDto(
            userId = "usr_9281a",
            username = "PublisherX",
            email = "publisher@senapoti.io",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
            credits = 450.0,
            xp = 1850,
            level = 4,
            rank = "Gold Publisher",
            verified = true,
            isVip = true,
            bio = "Digital marketer & Adsterra smartlink publisher. Scaling CPM and high-tier traffic.",
            referralCode = "SENAPOTI-9281",
            totalVisits = 342,
            todayVisits = 18,
            totalEarnings = 142.80,
            dailyLimit = 150
        )
    )
    val currentUser: StateFlow<UserDto> = _currentUser.asStateFlow()

    // Discover Community Links
    private val _discoverLinks = MutableStateFlow(
        listOf(
            LinkDto(
                id = "lnk_101",
                userId = "usr_ad_1",
                username = "ApexAds",
                title = "High CPM US Tech Smartlink",
                url = "https://beta.publishers.adsterra.com/smartlink/tech-us-tier1",
                category = "Adsterra Direct Link",
                creditsPerVisit = 5.0,
                dailyLimit = 500,
                todayVisits = 142,
                totalVisits = 2450,
                active = true,
                isAdsterra = true,
                estimatedCpm = 8.40
            ),
            LinkDto(
                id = "lnk_102",
                userId = "usr_ad_2",
                username = "TrafficWizard",
                title = "Global Gaming & Entertainment Landing",
                url = "https://landing.games-zone.org/play-now",
                category = "Smartlink",
                creditsPerVisit = 4.5,
                dailyLimit = 300,
                todayVisits = 88,
                totalVisits = 1120,
                active = true,
                isAdsterra = true,
                estimatedCpm = 5.20
            ),
            LinkDto(
                id = "lnk_103",
                userId = "usr_ad_3",
                username = "CryptoPulse",
                title = "Web3 Crypto & Fintech Review Portal",
                url = "https://cryptopulse.news/top-exchanges",
                category = "Finance",
                creditsPerVisit = 6.0,
                dailyLimit = 400,
                todayVisits = 194,
                totalVisits = 3890,
                active = true,
                isAdsterra = false,
                estimatedCpm = 11.50
            ),
            LinkDto(
                id = "lnk_104",
                userId = "usr_ad_4",
                username = "ViralBuzz",
                title = "Trending Tech Gadgets & Reviews 2026",
                url = "https://techviralbuzz.net/gadgets-top",
                category = "Tech",
                creditsPerVisit = 4.0,
                dailyLimit = 250,
                todayVisits = 67,
                totalVisits = 940,
                active = true,
                isAdsterra = false,
                estimatedCpm = 4.10
            ),
            LinkDto(
                id = "lnk_105",
                userId = "usr_ad_5",
                username = "AdsterraMaster",
                title = "Tier-1 Clean Direct Social Traffic",
                url = "https://publishers.adsterra.com/direct/soc-clean",
                category = "Adsterra Direct Link",
                creditsPerVisit = 5.5,
                dailyLimit = 600,
                todayVisits = 230,
                totalVisits = 4120,
                active = true,
                isAdsterra = true,
                estimatedCpm = 9.80
            ),
            LinkDto(
                id = "lnk_106",
                userId = "usr_ad_6",
                username = "StreamNova",
                title = "Movie & Series Streaming Community Hub",
                url = "https://streamnova.io/trending",
                category = "General",
                creditsPerVisit = 3.5,
                dailyLimit = 200,
                todayVisits = 45,
                totalVisits = 670,
                active = true,
                isAdsterra = false,
                estimatedCpm = 3.30
            )
        )
    )
    val discoverLinks: StateFlow<List<LinkDto>> = _discoverLinks.asStateFlow()

    // User's Own Created Links
    private val _myLinks = MutableStateFlow(
        listOf(
            LinkDto(
                id = "my_lnk_1",
                userId = "usr_9281a",
                username = "PublisherX",
                title = "Senapoti Main Smartlink (Tier 1)",
                url = "https://beta.publishers.adsterra.com/referral/kAuU8ketNe",
                category = "Adsterra Direct Link",
                creditsPerVisit = 5.0,
                dailyLimit = 200,
                todayVisits = 42,
                totalVisits = 890,
                active = true,
                isAdsterra = true,
                estimatedCpm = 7.90
            ),
            LinkDto(
                id = "my_lnk_2",
                userId = "usr_9281a",
                username = "PublisherX",
                title = "Affiliate Software Landing Page",
                url = "https://senapotixchange.io/pro-tools",
                category = "Tech",
                creditsPerVisit = 4.0,
                dailyLimit = 100,
                todayVisits = 19,
                totalVisits = 430,
                active = true,
                isAdsterra = false,
                estimatedCpm = 4.50
            )
        )
    )
    val myLinks: StateFlow<List<LinkDto>> = _myLinks.asStateFlow()

    // Level & Tasks
    private val _levelState = MutableStateFlow(
        LevelResponse(
            currentLevel = 4,
            currentXp = 1850,
            nextLevelXp = 2500,
            tierName = "Gold Publisher",
            tierMultiplier = 1.35,
            tasks = listOf(
                XpTask(
                    id = "tsk_1",
                    title = "Daily Explorer",
                    description = "Visit 10 publisher links today",
                    currentProgress = 10,
                    targetProgress = 10,
                    xpReward = 150,
                    creditReward = 20.0,
                    category = "Daily",
                    completed = true,
                    claimed = false
                ),
                XpTask(
                    id = "tsk_2",
                    title = "Traffic Booster",
                    description = "Visit 25 publisher links in Exchange Arena",
                    currentProgress = 18,
                    targetProgress = 25,
                    xpReward = 300,
                    creditReward = 40.0,
                    category = "Daily",
                    completed = false,
                    claimed = false
                ),
                XpTask(
                    id = "tsk_3",
                    title = "Campaign Architect",
                    description = "Create and launch at least 2 active campaigns",
                    currentProgress = 2,
                    targetProgress = 2,
                    xpReward = 400,
                    creditReward = 50.0,
                    category = "Weekly",
                    completed = true,
                    claimed = true
                ),
                XpTask(
                    id = "tsk_4",
                    title = "Network Builder",
                    description = "Invite 3 active referral publishers",
                    currentProgress = 2,
                    targetProgress = 3,
                    xpReward = 600,
                    creditReward = 100.0,
                    category = "Milestone",
                    completed = false,
                    claimed = false
                ),
                XpTask(
                    id = "tsk_5",
                    title = "Century Club",
                    description = "Reach 500 total successful link visits",
                    currentProgress = 342,
                    targetProgress = 500,
                    xpReward = 1000,
                    creditReward = 200.0,
                    category = "Milestone",
                    completed = false,
                    claimed = false
                )
            )
        )
    )
    val levelState: StateFlow<LevelResponse> = _levelState.asStateFlow()

    // Membership Status
    private val _membershipStatus = MutableStateFlow(
        MembershipStatusDto(
            isVip = true,
            currentPlan = "VIP Publisher",
            expiresAt = System.currentTimeMillis() + (22L * 24 * 60 * 60 * 1000),
            daysRemaining = 22,
            visitMultiplier = 1.35,
            priorityQueue = true
        )
    )
    val membershipStatus: StateFlow<MembershipStatusDto> = _membershipStatus.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationDto(
                id = "notif_1",
                title = "Exchange Reward Claimed",
                message = "You earned +5.0 Credits and +25 XP from visiting High CPM US Tech Smartlink.",
                timestamp = System.currentTimeMillis() - 15 * 60 * 1000,
                read = false,
                type = "REWARD"
            ),
            NotificationDto(
                id = "notif_2",
                title = "Daily Quest Unlocked",
                message = "You completed Daily Explorer quest! Claim +150 XP and +20 Credits.",
                timestamp = System.currentTimeMillis() - 45 * 60 * 1000,
                read = false,
                type = "LEVEL_UP"
            ),
            NotificationDto(
                id = "notif_3",
                title = "Referral Commission",
                message = "Your referral @DigitalPulse visited 10 links. You received +5.0 credits commission.",
                timestamp = System.currentTimeMillis() - 3 * 3600 * 1000,
                read = true,
                type = "REFERRAL"
            ),
            NotificationDto(
                id = "notif_4",
                title = "Adsterra CPM Spike",
                message = "Your US direct link traffic achieved an average CPM of $8.40 today.",
                timestamp = System.currentTimeMillis() - 6 * 3600 * 1000,
                read = true,
                type = "CAMPAIGN"
            )
        )
    )
    val notifications: StateFlow<List<NotificationDto>> = _notifications.asStateFlow()

    // Referral State
    private val _referralData = MutableStateFlow(
        ReferralResponse(
            referralCode = "SENAPOTI-9281",
            referralLink = "https://senapotixchange.io/join?ref=SENAPOTI-9281",
            totalReferrals = 14,
            activeReferrals = 9,
            totalEarnedCredits = 385.0,
            commissionRatePercent = 10.0,
            referredUsers = listOf(
                ReferredUserDto("usr_ref_1", "DigitalPulse", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=120", "2026-08-28", 120, 60.0, true),
                ReferredUserDto("usr_ref_2", "NeoPublisher", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120", "2026-08-29", 94, 47.0, true),
                ReferredUserDto("usr_ref_3", "CpmMaster77", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=120", "2026-09-01", 62, 31.0, true),
                ReferredUserDto("usr_ref_4", "SocialBooster", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=120", "2026-09-02", 15, 7.5, false)
            )
        )
    )
    val referralData: StateFlow<ReferralResponse> = _referralData.asStateFlow()

    // Adsterra API & Account Connection State
    private val _adsterraAccount = MutableStateFlow(
        AdsterraAccountDto(
            publisherId = "PUB-849201",
            email = "publisher.vip@adsterra-hub.net",
            apiKey = "ads_pub_849201_99a82e184f09a",
            isConnected = true,
            balance = 482.90,
            payoutMethod = "USDT (TRC20)",
            nextPayoutDate = "Sep 16, 2026",
            totalPlacements = 6,
            activeSmartLinks = 5,
            lastSyncedTimestamp = System.currentTimeMillis()
        )
    )
    val adsterraAccount: StateFlow<AdsterraAccountDto> = _adsterraAccount.asStateFlow()

    private val _adsterraApiStatus = MutableStateFlow(AdsterraApiStatus.CONNECTED)
    val adsterraApiStatus: StateFlow<AdsterraApiStatus> = _adsterraApiStatus.asStateFlow()

    private val _isSyncingAdsterra = MutableStateFlow(false)
    val isSyncingAdsterra: StateFlow<Boolean> = _isSyncingAdsterra.asStateFlow()

    // Adsterra Smart Links
    private val _adsterraSmartLinks = MutableStateFlow(
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
                isImportedToExchange = true,
                importedCampaignId = "my_lnk_1"
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
    )
    val adsterraSmartLinks: StateFlow<List<AdsterraSmartLinkDto>> = _adsterraSmartLinks.asStateFlow()

    // Hourly & Placement Stats
    private val _adsterraPlacementStats = MutableStateFlow(
        listOf(
            PlacementRealtimeStat("plc_894103", "Fintech & Crypto Smartlink", "Smartlink", 2980, 310, 10.4, 11.61, 34.60, "US (64%)"),
            PlacementRealtimeStat("plc_894101", "DirectLink US/Tier1 High-CPM", "Direct Link", 3280, 295, 9.0, 8.67, 28.45, "US (82%)"),
            PlacementRealtimeStat("plc_894102", "Global Smartlink Tech", "Smartlink", 2650, 184, 6.9, 6.87, 18.20, "GB (45%)"),
            PlacementRealtimeStat("plc_894104", "Social Bar Clean Traffic", "Social Bar", 2100, 142, 6.7, 6.10, 12.80, "CA (38%)"),
            PlacementRealtimeStat("plc_894105", "Popunder Main Website", "Popunder", 1850, 95, 5.1, 5.08, 9.40, "DE (51%)")
        )
    )
    val adsterraPlacementStats: StateFlow<List<PlacementRealtimeStat>> = _adsterraPlacementStats.asStateFlow()

    private val _adsterraHourlyStats = MutableStateFlow(
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
    )
    val adsterraHourlyStats: StateFlow<List<HourlyStatPoint>> = _adsterraHourlyStats.asStateFlow()

    // Adsterra Analytics State
    private val _selectedRange = MutableStateFlow(AdsterraDateRange.LAST_7_DAYS)
    val selectedRange: StateFlow<AdsterraDateRange> = _selectedRange.asStateFlow()

    fun setDateRange(range: AdsterraDateRange) {
        _selectedRange.value = range
    }

    fun connectAdsterraApi(apiKey: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        coroutineScope.launch {
            _adsterraApiStatus.value = AdsterraApiStatus.CONNECTING
            val result = adsterraApiService.connectWithApiKey(apiKey)
            if (result.isSuccess) {
                val account = result.getOrThrow()
                _adsterraAccount.value = account
                _adsterraApiStatus.value = AdsterraApiStatus.CONNECTED

                // Fetch smartlinks & stats
                val smartLinks = adsterraApiService.fetchAccountSmartLinks(apiKey)
                _adsterraSmartLinks.value = smartLinks
                _adsterraPlacementStats.value = adsterraApiService.fetchPlacementBreakdown()
                _adsterraHourlyStats.value = adsterraApiService.fetchHourlyPerformance()

                val notif = NotificationDto(
                    id = UUID.randomUUID().toString(),
                    title = "Adsterra API Connected",
                    message = "Successfully linked Adsterra Publisher ID ${account.publisherId} with ${_adsterraSmartLinks.value.size} smart links ready to import.",
                    timestamp = System.currentTimeMillis(),
                    read = false,
                    type = "CAMPAIGN"
                )
                _notifications.update { listOf(notif) + it }
                onSuccess()
            } else {
                _adsterraApiStatus.value = AdsterraApiStatus.ERROR
                onError(result.exceptionOrNull()?.message ?: "Failed to connect to Adsterra API")
            }
        }
    }

    fun disconnectAdsterraApi() {
        _adsterraAccount.update { it.copy(isConnected = false, apiKey = "") }
        _adsterraApiStatus.value = AdsterraApiStatus.DISCONNECTED
    }

    fun refreshAdsterraData() {
        coroutineScope.launch {
            _isSyncingAdsterra.value = true
            val apiKey = _adsterraAccount.value.apiKey
            val updatedLinks = adsterraApiService.fetchAccountSmartLinks(apiKey)
            _adsterraSmartLinks.value = updatedLinks
            _adsterraPlacementStats.value = adsterraApiService.fetchPlacementBreakdown()
            _adsterraHourlyStats.value = adsterraApiService.fetchHourlyPerformance()
            _adsterraAccount.update { it.copy(lastSyncedTimestamp = System.currentTimeMillis()) }
            _isSyncingAdsterra.value = false
        }
    }

    // Import a single Smart Link into Senapoti Exchange Campaigns
    fun importSmartLinkToCampaign(
        placementId: String,
        customTitle: String? = null,
        category: String = "Adsterra Direct Link",
        creditsPerVisit: Double = 5.0,
        dailyLimit: Int = 250
    ): LinkDto? {
        val smartLink = _adsterraSmartLinks.value.find { it.placementId == placementId } ?: return null
        val campaignTitle = customTitle?.takeIf { it.isNotBlank() } ?: smartLink.name

        val newCampaign = LinkDto(
            id = "ad_imp_" + UUID.randomUUID().toString().take(6),
            userId = _currentUser.value.userId,
            username = _currentUser.value.username,
            title = campaignTitle,
            url = smartLink.url,
            category = category,
            creditsPerVisit = creditsPerVisit,
            dailyLimit = dailyLimit,
            todayVisits = 0,
            totalVisits = 0,
            active = true,
            createdAt = System.currentTimeMillis(),
            isAdsterra = true,
            estimatedCpm = smartLink.currentCpm
        )

        // Add to user links and discover pool
        _myLinks.update { listOf(newCampaign) + it }
        _discoverLinks.update { listOf(newCampaign) + it }

        // Mark smartlink as imported
        _adsterraSmartLinks.update { list ->
            list.map {
                if (it.placementId == placementId) it.copy(isImportedToExchange = true, importedCampaignId = newCampaign.id) else it
            }
        }

        val notif = NotificationDto(
            id = UUID.randomUUID().toString(),
            title = "Smart Link Imported",
            message = "Imported '${smartLink.name}' to Exchange Arena at $creditsPerVisit credits/visit.",
            timestamp = System.currentTimeMillis(),
            read = false,
            type = "CAMPAIGN"
        )
        _notifications.update { listOf(notif) + it }

        return newCampaign
    }

    // Batch Import All Smart Links into Exchange Campaigns
    fun importAllSmartLinks(creditsPerVisit: Double = 5.0, dailyLimit: Int = 200): Int {
        val nonImported = _adsterraSmartLinks.value.filter { !it.isImportedToExchange }
        if (nonImported.isEmpty()) return 0

        val newCampaigns = nonImported.map { sl ->
            LinkDto(
                id = "ad_imp_" + UUID.randomUUID().toString().take(6),
                userId = _currentUser.value.userId,
                username = _currentUser.value.username,
                title = sl.name,
                url = sl.url,
                category = if (sl.format == "Direct Link") "Adsterra Direct Link" else "Smartlink",
                creditsPerVisit = creditsPerVisit,
                dailyLimit = dailyLimit,
                todayVisits = 0,
                totalVisits = 0,
                active = true,
                createdAt = System.currentTimeMillis(),
                isAdsterra = true,
                estimatedCpm = sl.currentCpm
            )
        }

        _myLinks.update { newCampaigns + it }
        _discoverLinks.update { newCampaigns + it }

        _adsterraSmartLinks.update { list ->
            list.map { sl ->
                val matching = newCampaigns.find { it.url == sl.url }
                if (matching != null) sl.copy(isImportedToExchange = true, importedCampaignId = matching.id) else sl
            }
        }

        val notif = NotificationDto(
            id = UUID.randomUUID().toString(),
            title = "Batch Smart Links Imported",
            message = "Successfully imported ${newCampaigns.size} Adsterra Smart Links into active Exchange Campaigns!",
            timestamp = System.currentTimeMillis(),
            read = false,
            type = "CAMPAIGN"
        )
        _notifications.update { listOf(notif) + it }

        return newCampaigns.size
    }

    fun getAdsterraStats(range: AdsterraDateRange = _selectedRange.value): StatsResponse {
        val multiplier = when (range) {
            AdsterraDateRange.TODAY -> 0.3
            AdsterraDateRange.YESTERDAY -> 0.35
            AdsterraDateRange.LAST_7_DAYS -> 1.0
            AdsterraDateRange.LAST_30_DAYS -> 3.8
            AdsterraDateRange.MONTH_TO_DATE -> 1.4
        }
        val summary = AdsterraSummary(
            todayRevenue = 24.85,
            yesterdayRevenue = 28.40,
            sevenDayRevenue = 184.20 * multiplier,
            monthRevenue = 642.50 * multiplier,
            todayCpm = 7.82,
            averageCpm = 6.95,
            totalImpressions = (26500 * multiplier).toInt(),
            totalClicks = (1890 * multiplier).toInt(),
            ctr = 7.13
        )
        val points = listOf(
            RevenueDataPoint("Aug 28", 22.40 * multiplier, 3200, 240, 7.00),
            RevenueDataPoint("Aug 29", 26.80 * multiplier, 3800, 275, 7.05),
            RevenueDataPoint("Aug 30", 24.10 * multiplier, 3400, 250, 7.08),
            RevenueDataPoint("Aug 31", 29.50 * multiplier, 4100, 298, 7.19),
            RevenueDataPoint("Sep 01", 27.20 * multiplier, 3900, 280, 6.97),
            RevenueDataPoint("Sep 02", 28.40 * multiplier, 4050, 290, 7.01),
            RevenueDataPoint("Sep 03", 24.85 * multiplier, 4050, 285, 7.82)
        )
        val countries = listOf(
            CountryStat("US", "United States", 84.50 * multiplier, 9800, 810, 8.62),
            CountryStat("GB", "United Kingdom", 38.20 * multiplier, 4900, 390, 7.79),
            CountryStat("CA", "Canada", 27.60 * multiplier, 3600, 280, 7.66),
            CountryStat("DE", "Germany", 18.90 * multiplier, 2900, 210, 6.51),
            CountryStat("BR", "Brazil", 9.40 * multiplier, 3100, 120, 3.03),
            CountryStat("IN", "India", 5.60 * multiplier, 2200, 80, 2.54)
        )
        return StatsResponse(summary, points, countries)
    }

    // Community Leaderboard
    fun getCommunityLeaderboard(): List<LeaderboardEntry> = listOf(
        LeaderboardEntry(1, "usr_10", "AdKing_Official", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120", 12, 14250.0, true, "Master"),
        LeaderboardEntry(2, "usr_11", "GlobalTrafficX", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120", 10, 11890.0, true, "Elite"),
        LeaderboardEntry(3, "usr_12", "CpmHunter", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=120", 9, 9740.0, true, "Diamond"),
        LeaderboardEntry(4, "usr_9281a", "PublisherX (You)", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=120", 4, 3850.0, true, "Gold"),
        LeaderboardEntry(5, "usr_13", "SmartlinkPro", "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=120", 8, 3420.0, false, "Platinum"),
        LeaderboardEntry(6, "usr_14", "AdNinja", "https://images.unsplash.com/photo-1628157582853-a796fa650a6a?w=120", 7, 2980.0, false, "Gold"),
        LeaderboardEntry(7, "usr_15", "TrafficFlow", "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=120", 6, 2410.0, false, "Silver")
    )

    // Adsterra Revenue Leaderboard
    fun getAdsterraLeaderboard(): List<AdsterraLeaderboardEntry> = listOf(
        AdsterraLeaderboardEntry(1, "usr_10", "AdKing_Official", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120", 1485.40, 185000, 14200, true),
        AdsterraLeaderboardEntry(2, "usr_11", "GlobalTrafficX", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120", 1120.90, 142000, 11050, true),
        AdsterraLeaderboardEntry(3, "usr_12", "CpmHunter", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=120", 940.30, 118000, 9300, true),
        AdsterraLeaderboardEntry(4, "usr_13", "SmartlinkPro", "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=120", 680.15, 89000, 6400, false),
        AdsterraLeaderboardEntry(5, "usr_9281a", "PublisherX (You)", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=120", 442.80, 58000, 4120, true),
        AdsterraLeaderboardEntry(6, "usr_14", "AdNinja", "https://images.unsplash.com/photo-1628157582853-a796fa650a6a?w=120", 390.50, 51000, 3800, false)
    )

    // Shop Products
    fun getProducts(): List<ProductDto> = listOf(
        ProductDto("prod_1", "Starter Pack", 500.0, 50.0, 4.99, false, "550 Total Credits for rapid campaign kickstart"),
        ProductDto("prod_2", "Publisher Growth", 2000.0, 350.0, 14.99, true, "2,350 Total Credits + Priority queue boost"),
        ProductDto("prod_3", "Pro Campaigner", 5000.0, 1200.0, 34.99, false, "6,200 Total Credits + VIP badge + 24/7 Priority"),
        ProductDto("prod_4", "Enterprise Agency", 20000.0, 6000.0, 119.99, false, "26,000 Total Credits + Dedicated account manager")
    )

    // Membership Plans
    fun getMembershipPlans(): List<MembershipPlanDto> = listOf(
        MembershipPlanDto(
            id = "plan_free",
            name = "Free Pioneer",
            price = 0.0,
            durationDays = 365,
            visitMultiplier = 1.0,
            dailyLimitBoost = 0,
            priorityQueue = false,
            adFree = false,
            badge = "Free",
            features = listOf("1.0x Credit Payout", "100 Daily visits limit", "Standard link queue", "Community support")
        ),
        MembershipPlanDto(
            id = "plan_vip",
            name = "VIP Publisher",
            price = 12.99,
            durationDays = 30,
            visitMultiplier = 1.35,
            dailyLimitBoost = 150,
            priorityQueue = true,
            adFree = true,
            badge = "VIP Gold",
            features = listOf("1.35x Credit Multiplier", "250 Daily visits limit", "Priority queue in Discover", "Ad-Free Arena", "Golden VIP Badge")
        ),
        MembershipPlanDto(
            id = "plan_elite",
            name = "Elite Agency",
            price = 29.99,
            durationDays = 30,
            visitMultiplier = 1.60,
            dailyLimitBoost = 500,
            priorityQueue = true,
            adFree = true,
            badge = "Elite Diamond",
            features = listOf("1.60x Credit Multiplier", "Unlimited daily visits", "Top-tier instant link rotation", "Exclusive high-CPM Adsterra smartlinks", "24/7 VIP Support")
        )
    )

    // Actions
    fun completeLinkVisit(linkId: String): VisitResponse {
        val link = _discoverLinks.value.find { it.id == linkId }
        val multiplier = _membershipStatus.value.visitMultiplier
        val rewardCredits = (link?.creditsPerVisit ?: 5.0) * multiplier
        val rewardXp = 25

        _currentUser.update { user ->
            user.copy(
                credits = user.credits + rewardCredits,
                xp = user.xp + rewardXp,
                todayVisits = user.todayVisits + 1,
                totalVisits = user.totalVisits + 1
            )
        }

        // Add notification
        val newNotif = NotificationDto(
            id = UUID.randomUUID().toString(),
            title = "Exchange Completed",
            message = "Earned +${String.format("%.1f", rewardCredits)} Credits & +$rewardXp XP for visiting '${link?.title ?: "Publisher Link"}'.",
            timestamp = System.currentTimeMillis(),
            read = false,
            type = "REWARD"
        )
        _notifications.update { listOf(newNotif) + it }

        // Update task progress if any
        _levelState.update { state ->
            val updatedTasks = state.tasks.map { task ->
                if (task.id == "tsk_2" && !task.completed) {
                    val nextProg = task.currentProgress + 1
                    task.copy(
                        currentProgress = nextProg,
                        completed = nextProg >= task.targetProgress
                    )
                } else task
            }
            state.copy(
                currentXp = state.currentXp + rewardXp,
                tasks = updatedTasks
            )
        }

        return VisitResponse(
            success = true,
            creditsEarned = rewardCredits,
            xpEarned = rewardXp,
            newBalance = _currentUser.value.credits,
            message = "Visit verified! +${String.format("%.1f", rewardCredits)} credits added."
        )
    }

    fun createLink(request: CreateLinkRequest): LinkDto {
        val newLink = LinkDto(
            id = "my_lnk_" + UUID.randomUUID().toString().take(6),
            userId = _currentUser.value.userId,
            username = _currentUser.value.username,
            title = request.title,
            url = request.url,
            category = request.category,
            creditsPerVisit = request.creditsPerVisit,
            dailyLimit = request.dailyLimit,
            todayVisits = 0,
            totalVisits = 0,
            active = true,
            createdAt = System.currentTimeMillis(),
            isAdsterra = request.isAdsterra,
            estimatedCpm = if (request.isAdsterra) 6.5 else 4.0
        )
        _myLinks.update { listOf(newLink) + it }
        _discoverLinks.update { listOf(newLink) + it }

        val notif = NotificationDto(
            id = UUID.randomUUID().toString(),
            title = "Campaign Launched",
            message = "Your campaign '${request.title}' is now active in the Exchange pool.",
            timestamp = System.currentTimeMillis(),
            read = false,
            type = "CAMPAIGN"
        )
        _notifications.update { listOf(notif) + it }
        return newLink
    }

    fun toggleLinkActive(linkId: String) {
        _myLinks.update { list ->
            list.map { if (it.id == linkId) it.copy(active = !it.active) else it }
        }
        _discoverLinks.update { list ->
            list.map { if (it.id == linkId) it.copy(active = !it.active) else it }
        }
    }

    fun deleteLink(linkId: String) {
        _myLinks.update { list -> list.filter { it.id != linkId } }
        _discoverLinks.update { list -> list.filter { it.id != linkId } }
    }

    fun claimTaskReward(taskId: String) {
        _levelState.update { state ->
            val targetTask = state.tasks.find { it.id == taskId } ?: return@update state
            if (!targetTask.completed || targetTask.claimed) return@update state

            _currentUser.update { u ->
                u.copy(
                    credits = u.credits + targetTask.creditReward,
                    xp = u.xp + targetTask.xpReward
                )
            }

            val updatedTasks = state.tasks.map {
                if (it.id == taskId) it.copy(claimed = true) else it
            }
            state.copy(tasks = updatedTasks)
        }
    }

    fun purchaseCredits(product: ProductDto, paymentMethod: String): OrderDto {
        val totalCredits = product.creditsAmount + product.bonusCredits
        _currentUser.update { it.copy(credits = it.credits + totalCredits) }

        val order = OrderDto(
            orderId = "ORD-" + UUID.randomUUID().toString().take(8).uppercase(),
            productName = product.name,
            amount = totalCredits,
            priceUsd = product.priceUsd,
            paymentMethod = paymentMethod,
            timestamp = System.currentTimeMillis(),
            status = "COMPLETED"
        )

        val notif = NotificationDto(
            id = UUID.randomUUID().toString(),
            title = "Credits Purchased",
            message = "Successfully added +$totalCredits Credits via $paymentMethod.",
            timestamp = System.currentTimeMillis(),
            read = false,
            type = "REWARD"
        )
        _notifications.update { listOf(notif) + it }
        return order
    }

    fun upgradeMembership(plan: MembershipPlanDto) {
        _membershipStatus.update {
            it.copy(
                isVip = plan.price > 0,
                currentPlan = plan.name,
                daysRemaining = plan.durationDays,
                visitMultiplier = plan.visitMultiplier,
                priorityQueue = plan.priorityQueue
            )
        }
        _currentUser.update { it.copy(isVip = plan.price > 0, rank = plan.name) }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list -> list.map { it.copy(read = true) } }
    }

    fun updateProfile(username: String, bio: String) {
        _currentUser.update { it.copy(username = username, bio = bio) }
    }
}
