package com.senapotixchange.ui.screens.visit

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.senapotixchange.data.model.LinkDto
import com.senapotixchange.data.model.VisitResponse
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun VisitScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val discoverLinks by repository.discoverLinks.collectAsState()
    val membershipStatus by repository.membershipStatus.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var activeVisitingLink by remember { mutableStateOf<LinkDto?>(null) }
    var lastVisitResult by remember { mutableStateOf<VisitResponse?>(null) }
    var autoAdvance by remember { mutableStateOf(false) }

    val categories = listOf("All", "Adsterra Direct Link", "Smartlink", "Finance", "Tech", "General")

    val filteredLinks = discoverLinks.filter { link ->
        val matchesCategory = selectedCategory == "All" || link.category == selectedCategory
        val matchesSearch = link.title.contains(searchQuery, ignoreCase = true) ||
                link.username.contains(searchQuery, ignoreCase = true)
        link.active && matchesCategory && matchesSearch
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Header Banner
            item {
                NebulaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF131C30),
                    borderColor = PrimaryBlue.copy(alpha = 0.5f)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Exchange Arena",
                                    color = TextPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Visit publishers & earn credits instantly",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            // Multiplier Tag
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentGold.copy(alpha = 0.15f))
                                    .border(1.dp, AccentGold, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${membershipStatus.visitMultiplier}x Payout Active",
                                    color = AccentGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search campaigns or publishers...", color = TextMuted, fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SurfaceCard,
                                unfocusedContainerColor = SurfaceCard,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = SurfaceCardBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }
            }

            // Categories Filter
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        NebulaChip(
                            text = cat,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }
            }

            // Available Campaigns Count & Auto-advance
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredLinks.size} Campaigns Available",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { autoAdvance = !autoAdvance }
                    ) {
                        Text(
                            text = "Auto-Advance",
                            color = if (autoAdvance) AccentGreen else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = autoAdvance,
                            onCheckedChange = { autoAdvance = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentGreen,
                                uncheckedThumbColor = TextSecondary,
                                uncheckedTrackColor = SurfaceCard
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }

            // Links List
            items(filteredLinks, key = { it.id }) { link ->
                NebulaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceCard
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (link.isAdsterra) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(PrimaryPurple.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("ADSTERRA", color = PrimaryPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = link.category,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = link.title,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "By @${link.username} • Est. CPM $${String.format("%.2f", link.estimatedCpm)}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            // Reward Badge
                            Column(horizontalAlignment = Alignment.End) {
                                val reward = link.creditsPerVisit * membershipStatus.visitMultiplier
                                Text(
                                    text = "+${String.format("%.1f", reward)} Cr",
                                    color = AccentGold,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "+25 XP",
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${link.todayVisits}/${link.dailyLimit} visits today",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Button(
                                onClick = { activeVisitingLink = link },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = null,
                                    tint = TextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Visit (15s)", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Active Visiting Modal / Webview Simulator Dialog
        if (activeVisitingLink != null) {
            VisitingModalDialog(
                link = activeVisitingLink!!,
                multiplier = membershipStatus.visitMultiplier,
                onDismiss = { activeVisitingLink = null },
                onComplete = {
                    val result = repository.completeLinkVisit(activeVisitingLink!!.id)
                    lastVisitResult = result
                    val currentIndex = filteredLinks.indexOf(activeVisitingLink)
                    if (autoAdvance && currentIndex != -1 && currentIndex < filteredLinks.size - 1) {
                        activeVisitingLink = filteredLinks[currentIndex + 1]
                    } else {
                        activeVisitingLink = null
                    }
                }
            )
        }

        // Reward Snackbar / Toast
        if (lastVisitResult != null) {
            LaunchedEffect(lastVisitResult) {
                delay(3000)
                lastVisitResult = null
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp, start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCardElevated)
                    .border(1.dp, AccentGreen, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${lastVisitResult?.message} (+${lastVisitResult?.xpEarned} XP)",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun VisitingModalDialog(
    link: LinkDto,
    multiplier: Double,
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(15) }
    var isTimerFinished by remember { mutableStateOf(false) }

    LaunchedEffect(link.id) {
        secondsLeft = 15
        isTimerFinished = false
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
        isTimerFinished = true
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark.copy(alpha = 0.95f))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            ) {
                // Top Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceCard)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = link.title,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Text(
                            text = link.url,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }

                    // Anti-Cheat / Clean Traffic Indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Clean Ad Traffic",
                            color = AccentGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = { (15 - secondsLeft) / 15f },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = if (isTimerFinished) AccentGreen else PrimaryBlue,
                    trackColor = SurfaceCardBorder
                )

                // Simulated In-App Publisher Webview Body
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF0F1420))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.15f))
                                .border(2.dp, if (isTimerFinished) AccentGreen else PrimaryBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isTimerFinished) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = AccentGreen,
                                    modifier = Modifier.size(40.dp)
                                )
                            } else {
                                Text(
                                    text = "${secondsLeft}s",
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isTimerFinished) "Verification Successful!" else "Verifying Active Traffic...",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isTimerFinished)
                                "You can now claim your reward credits and XP."
                            else
                                "Stay on this page while publisher impressions and CPM telemetry are recorded.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Target URL box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceCard)
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = AccentGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = link.url,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // Bottom Action Footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceCard)
                        .padding(16.dp)
                ) {
                    val reward = link.creditsPerVisit * multiplier
                    Button(
                        onClick = onComplete,
                        enabled = isTimerFinished,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen,
                            disabledContainerColor = SurfaceCardBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isTimerFinished)
                                "Claim +${String.format("%.1f", reward)} Credits & Complete"
                            else
                                "Please wait (${secondsLeft}s left)",
                            color = if (isTimerFinished) Color.Black else TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
