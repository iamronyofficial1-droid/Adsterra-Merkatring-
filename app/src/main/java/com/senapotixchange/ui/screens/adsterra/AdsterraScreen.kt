package com.senapotixchange.ui.screens.adsterra

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.senapotixchange.data.model.*
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*

@Composable
fun AdsterraScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val account by repository.adsterraAccount.collectAsState()
    val apiStatus by repository.adsterraApiStatus.collectAsState()
    val isSyncing by repository.isSyncingAdsterra.collectAsState()
    val smartLinks by repository.adsterraSmartLinks.collectAsState()
    val placementStats by repository.adsterraPlacementStats.collectAsState()
    val hourlyStats by repository.adsterraHourlyStats.collectAsState()
    val selectedRange by repository.selectedRange.collectAsState()
    val stats = repository.getAdsterraStats(selectedRange)

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Analytics, 1: Smart Links, 2: Placements & Geo, 3: API Config
    var showConnectDialog by remember { mutableStateOf(false) }
    var smartLinkToImport by remember { mutableStateOf<AdsterraSmartLinkDto?>(null) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    fun copyText(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied!", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // 1. Hero Connection & Balance Card
            item {
                NebulaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF12192B),
                    borderColor = if (account.isConnected) PrimaryBlue.copy(alpha = 0.6f) else AccentRed.copy(alpha = 0.5f)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryPurple.copy(alpha = 0.2f))
                                        .border(1.5.dp, PrimaryPurple, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CloudSync, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Adsterra API Hub",
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (account.isConnected) AccentGreen else AccentRed)
                                        )
                                    }
                                    Text(
                                        text = if (account.isConnected) "ID: ${account.publisherId} • ${account.email}" else "Not Connected (API Key Required)",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (account.isConnected) {
                                    IconButton(
                                        onClick = {
                                            repository.refreshAdsterraData()
                                            Toast.makeText(context, "Synced with Adsterra API", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceCard)
                                    ) {
                                        if (isSyncing) {
                                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = PrimaryBlue, strokeWidth = 2.dp)
                                        } else {
                                            Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                Button(
                                    onClick = { showConnectDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (account.isConnected) SurfaceCardElevated else PrimaryBlue
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(
                                        text = if (account.isConnected) "API Key" else "Connect API",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        if (account.isConnected) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = SurfaceCardBorder, thickness = 0.8.dp)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ADSTERRA PAYOUT BALANCE", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "$${String.format("%.2f", account.balance)} USD",
                                        color = AccentGold,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("PAYOUT METHOD", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "${account.payoutMethod} • ${account.nextPayoutDate}",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Navigation Tabs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCard)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val tabs = listOf(
                        "Analytics" to Icons.Default.QueryStats,
                        "Smart Links (${smartLinks.size})" to Icons.Default.Link,
                        "Placements" to Icons.Default.PieChart,
                        "API Settings" to Icons.Default.VpnKey
                    )

                    tabs.forEachIndexed { index, (title, icon) ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryBlue else Color.Transparent)
                                .clickable { selectedTab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) TextPrimary else TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = title,
                                    color = if (isSelected) TextPrimary else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // TAB 0: REAL-TIME ANALYTICS
            if (selectedTab == 0) {
                // Date Range Filter Chips
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(AdsterraDateRange.values()) { range ->
                            NebulaChip(
                                text = range.label,
                                isSelected = selectedRange == range,
                                onClick = { repository.setDateRange(range) }
                            )
                        }
                    }
                }

                // Summary Metric Tiles Grid
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatMetricBox(
                            title = "Total Revenue",
                            value = "$${String.format("%.2f", stats.summary.sevenDayRevenue)}",
                            subtitle = "+16.8% vs last week",
                            icon = Icons.Default.AttachMoney,
                            accentColor = AccentGreen,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricBox(
                            title = "Average CPM",
                            value = "$${String.format("%.2f", stats.summary.averageCpm)}",
                            subtitle = "Peak: $11.61 CPM",
                            icon = Icons.Default.Speed,
                            accentColor = PrimaryBlue,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatMetricBox(
                            title = "Impressions",
                            value = "${stats.summary.totalImpressions}",
                            subtitle = "Real Adsterra traffic",
                            icon = Icons.Default.Visibility,
                            accentColor = PrimaryPurple,
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricBox(
                            title = "Clicks & CTR",
                            value = "${stats.summary.totalClicks} (${stats.summary.ctr}%)",
                            subtitle = "Direct conversion",
                            icon = Icons.Default.TouchApp,
                            accentColor = AccentGold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Hourly Performance Realtime Trend
                item {
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = SurfaceCard
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Hourly Real-Time Revenue", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("Today's revenue velocity by hour", color = TextSecondary, fontSize = 11.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AccentGreen.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("LIVE CPM", color = AccentGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            val maxHourlyRev = hourlyStats.maxOfOrNull { it.revenue } ?: 1.0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                hourlyStats.forEach { hr ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "$${String.format("%.1f", hr.revenue)}",
                                            color = TextSecondary,
                                            fontSize = 9.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(20.dp)
                                                .height(((hr.revenue / maxHourlyRev) * 85).coerceAtLeast(8.0).dp)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(AppGradients.Primary)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = hr.hour.take(2) + "h",
                                            color = TextMuted,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Daily Revenue Historical Bar Chart
                item {
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = SurfaceCard
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Daily Performance Breakdown", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("USD ($)", color = TextSecondary, fontSize = 11.sp)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            val maxRev = stats.revenuePoints.maxOfOrNull { it.revenue } ?: 1.0
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                stats.revenuePoints.forEach { pt ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "$${pt.revenue.toInt()}",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(22.dp)
                                                .height(((pt.revenue / maxRev) * 85).coerceAtLeast(10.0).dp)
                                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                                .background(PrimaryPurple)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = pt.date.takeLast(2),
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 1: SMART LINKS & IMPORT SYSTEM
            if (selectedTab == 1) {
                // Batch Import Banner
                item {
                    val notImportedCount = smartLinks.count { !it.isImportedToExchange }
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFF14243B),
                        borderColor = AccentCyan.copy(alpha = 0.5f)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Adsterra Smart Links Importer", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(
                                        text = "$notImportedCount new smart links available to import to Exchange Campaigns.",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(AccentCyan.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                                }
                            }

                            if (notImportedCount > 0) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        val count = repository.importAllSmartLinks(creditsPerVisit = 5.0, dailyLimit = 200)
                                        snackbarMessage = "Successfully imported $count Smart Links to active campaigns!"
                                        showSuccessSnackbar = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Import All ($notImportedCount) to Exchange Campaigns", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Text("Account Smart Links & Placements (${smartLinks.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                // List of Smart Links
                items(smartLinks, key = { it.placementId }) { sl ->
                    AdsterraSmartLinkItemCard(
                        smartLink = sl,
                        onCopyUrl = { copyText(sl.url, "Smartlink URL") },
                        onImportClick = { smartLinkToImport = sl }
                    )
                }
            }

            // TAB 2: PLACEMENTS & GEO ANALYTICS
            if (selectedTab == 2) {
                item {
                    Text("Placement Performance Breakdown", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                items(placementStats, key = { it.placementId }) { p ->
                    NebulaCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(p.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(PrimaryBlue.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(p.format, color = PrimaryBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Top Geo: ${p.topCountry} • ${p.impressions} Impr • ${p.clicks} Clicks", color = TextSecondary, fontSize = 11.sp)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("$${String.format("%.2f", p.revenue)}", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("$${String.format("%.2f", p.cpm)} CPM", color = AccentGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Top Country Geo Revenue & CPM", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                item {
                    NebulaCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            val maxCountryRev = stats.countryStats.maxOfOrNull { it.revenue } ?: 1.0
                            stats.countryStats.forEach { country ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(SurfaceCardElevated),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(country.countryCode, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(country.countryName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Text("CPM: $${String.format("%.2f", country.cpm)}", color = AccentGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text("$${String.format("%.2f", country.revenue)}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { (country.revenue / maxCountryRev).toFloat() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = PrimaryPurple,
                                        trackColor = SurfaceCardBorder
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }

            // TAB 3: API CONFIGURATION & CREDENTIALS
            if (selectedTab == 3) {
                item {
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = SurfaceCard
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VpnKey, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Adsterra API Configuration", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Current Connected API Token:", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCardElevated)
                                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (account.apiKey.isNotBlank()) account.apiKey.take(12) + "••••••••••••" + account.apiKey.takeLast(4) else "No API Key configured",
                                    color = if (account.apiKey.isNotBlank()) TextPrimary else TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                if (account.apiKey.isNotBlank()) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = AccentGreen, modifier = Modifier.size(18.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = { showConnectDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Change API Key", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                if (account.isConnected) {
                                    Button(
                                        onClick = {
                                            repository.disconnectAdsterraApi()
                                            Toast.makeText(context, "Disconnected Adsterra API", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed.copy(alpha = 0.2f)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Disconnect", color = AccentRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Guide on How to get API Key
                item {
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFF131A26),
                        borderColor = PrimaryBlue.copy(alpha = 0.3f)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("How to get your Adsterra API Key", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("1. Log in to your Adsterra Publisher account at publishers.adsterra.com", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("2. Navigate to Profile / Settings > API section.", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("3. Click 'Generate API Token' and copy your private key.", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("4. Paste it here to enable real-time smartlink sync & CPM telemetry.", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Connect API Dialog
        if (showConnectDialog) {
            ConnectAdsterraApiDialog(
                currentApiKey = account.apiKey,
                onDismiss = { showConnectDialog = false },
                onConnect = { key ->
                    repository.connectAdsterraApi(
                        apiKey = key,
                        onSuccess = {
                            showConnectDialog = false
                            snackbarMessage = "Connected to Adsterra API successfully!"
                            showSuccessSnackbar = true
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            )
        }

        // Single Smart Link Import Dialog
        if (smartLinkToImport != null) {
            ImportSmartLinkDialog(
                smartLink = smartLinkToImport!!,
                onDismiss = { smartLinkToImport = null },
                onConfirmImport = { title, category, credits, limit ->
                    repository.importSmartLinkToCampaign(
                        placementId = smartLinkToImport!!.placementId,
                        customTitle = title,
                        category = category,
                        creditsPerVisit = credits,
                        dailyLimit = limit
                    )
                    smartLinkToImport = null
                    snackbarMessage = "Imported '${title}' to Exchange Campaigns!"
                    showSuccessSnackbar = true
                }
            )
        }

        // Success Snackbar Notification
        if (showSuccessSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = AccentGreen,
                contentColor = Color.Black,
                action = {
                    TextButton(onClick = { showSuccessSnackbar = false }) {
                        Text("OK", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            ) {
                Text(snackbarMessage, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AdsterraSmartLinkItemCard(
    smartLink: AdsterraSmartLinkDto,
    onCopyUrl: () -> Unit,
    onImportClick: () -> Unit
) {
    NebulaCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceCard,
        borderColor = if (smartLink.isImportedToExchange) PrimaryBlue else SurfaceCardBorder
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(smartLink.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(PrimaryPurple.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(smartLink.format, color = PrimaryPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Placement ID: ${smartLink.placementId}", color = TextSecondary, fontSize = 11.sp)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AccentGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("$${String.format("%.2f", smartLink.currentCpm)} CPM", color = AccentGreen, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // URL Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCardElevated)
                    .clickable { onCopyUrl() }
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = smartLink.url,
                    color = PrimaryBlue,
                    fontSize = 11.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextSecondary, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats & Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text("TODAY REVENUE", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("$${String.format("%.2f", smartLink.todayRevenue)}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column {
                        Text("IMPRESSIONS", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("${smartLink.todayImpressions}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column {
                        Text("CLICKS", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("${smartLink.todayClicks}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                if (smartLink.isImportedToExchange) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Active Campaign", color = PrimaryBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Button(
                        onClick = onImportClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Import Link", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ConnectAdsterraApiDialog(
    currentApiKey: String,
    onDismiss: () -> Unit,
    onConnect: (String) -> Unit
) {
    var apiKeyInput by remember { mutableStateOf(currentApiKey.ifBlank { "ads_pub_849201_99a82e184f09a" }) }
    var isConnecting by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        NebulaCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceDark,
            borderColor = PrimaryBlue
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VpnKey, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Connect Adsterra Account", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Real-Time Publisher API & Smartlinks", color = TextSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = { apiKeyInput = it },
                    label = { Text("Adsterra API Token / Key", color = TextSecondary, fontSize = 12.sp) },
                    placeholder = { Text("e.g. ads_pub_xxxx_xxxx", color = TextMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { apiKeyInput = "ads_pub_849201_99a82e184f09a" },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("Fill Demo API Token", color = AccentGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            isConnecting = true
                            onConnect(apiKeyInput)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isConnecting) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Verify & Connect", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImportSmartLinkDialog(
    smartLink: AdsterraSmartLinkDto,
    onDismiss: () -> Unit,
    onConfirmImport: (title: String, category: String, credits: Double, dailyLimit: Int) -> Unit
) {
    var title by remember { mutableStateOf(smartLink.name) }
    var selectedCategory by remember { mutableStateOf(if (smartLink.format == "Direct Link") "Adsterra Direct Link" else "Smartlink") }
    var creditsPerVisit by remember { mutableFloatStateOf(5.0f) }
    var dailyLimit by remember { mutableFloatStateOf(250f) }

    Dialog(onDismissRequest = onDismiss) {
        NebulaCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceDark,
            borderColor = PrimaryBlue
        ) {
            Column {
                Text("Import Smart Link to Exchange", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Configure your traffic exchange campaign parameters", color = TextSecondary, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Campaign Title", color = TextSecondary, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Credits Per Visit Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Credits Reward / Visit", color = TextSecondary, fontSize = 12.sp)
                    Text("${String.format("%.1f", creditsPerVisit)} Credits", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Slider(
                    value = creditsPerVisit,
                    onValueChange = { creditsPerVisit = it },
                    valueRange = 2f..15f,
                    steps = 12,
                    colors = SliderDefaults.colors(thumbColor = AccentGold, activeTrackColor = AccentGold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Daily Limit Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Daily Visits Cap", color = TextSecondary, fontSize = 12.sp)
                    Text("${dailyLimit.toInt()} visits/day", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Slider(
                    value = dailyLimit,
                    onValueChange = { dailyLimit = it },
                    valueRange = 50f..1000f,
                    steps = 18,
                    colors = SliderDefaults.colors(thumbColor = PrimaryBlue, activeTrackColor = PrimaryBlue)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirmImport(title, selectedCategory, creditsPerVisit.toDouble(), dailyLimit.toInt())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Confirm Import", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
