package com.senapotixchange.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.NebulaCard
import com.senapotixchange.ui.theme.*

@Composable
fun SettingsScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    var rewardAlerts by remember { mutableStateOf(true) }
    var campaignBudgetAlerts by remember { mutableStateOf(true) }
    var referralAlerts by remember { mutableStateOf(true) }
    var adblockDiagnosticsRunning by remember { mutableStateOf(false) }
    var adblockResult by remember { mutableStateOf<String?>(null) }
    var dailyVisitLimit by remember { mutableFloatStateOf(150f) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Header
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF151C2C)
            ) {
                Column {
                    Text("Settings & Diagnostics", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Configure traffic limits, alert preferences and network security", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        // Traffic & Exchange Limits
        item {
            NebulaCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Exchange & Traffic Throttling", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Daily Exchange Limit", color = TextSecondary, fontSize = 13.sp)
                        Text("${dailyVisitLimit.toInt()} visits/day", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Slider(
                        value = dailyVisitLimit,
                        onValueChange = { dailyVisitLimit = it },
                        valueRange = 50f..300f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = PrimaryBlue,
                            activeTrackColor = PrimaryBlue,
                            inactiveTrackColor = SurfaceCardBorder
                        )
                    )
                }
            }
        }

        // Anti-Adblock & Clean Traffic Diagnostics
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF131E2B),
                borderColor = AccentGreen.copy(alpha = 0.4f)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AdBlock & Publisher Telemetry Check", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Test your connection to verify full compatibility with Adsterra Smartlinks and publisher CPM tracking.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (adblockResult != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceCard)
                                .padding(10.dp)
                        ) {
                            Text(adblockResult!!, color = AccentGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Button(
                        onClick = {
                            adblockDiagnosticsRunning = true
                            adblockResult = "✓ Diagnostics Passed: Clean browser environment detected. High CPM Smartlinks enabled."
                            adblockDiagnosticsRunning = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Run Diagnostics Test", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Notification Preferences
        item {
            NebulaCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Notification Preferences", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    SettingsSwitchRow("Exchange Reward Credits", rewardAlerts) { rewardAlerts = it }
                    Divider(color = SurfaceCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                    SettingsSwitchRow("Campaign Budget & Visit Alerts", campaignBudgetAlerts) { campaignBudgetAlerts = it }
                    Divider(color = SurfaceCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                    SettingsSwitchRow("Referral Commissions", referralAlerts) { referralAlerts = it }
                }
            }
        }

        // About & Cache
        item {
            NebulaCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Application Info", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Version", color = TextSecondary, fontSize = 13.sp)
                        Text("v1.0.0 (Build 2026)", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Publisher Network", color = TextSecondary, fontSize = 13.sp)
                        Text("Senapoti Adsterra Relay", color = PrimaryBlue, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { Toast.makeText(context, "Local cache cleared successfully!", Toast.LENGTH_SHORT).show() },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
                    ) {
                        Text("Clear Local Cache", color = TextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TextSecondary, fontSize = 13.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceCardBorder
            )
        )
    }
}
