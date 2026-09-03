package com.senapotixchange.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senapotixchange.data.model.UserDto
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.navigation.Screen
import com.senapotixchange.ui.theme.*

@Composable
fun DashboardScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val levelState by repository.levelState.collectAsState()
    val myLinks by repository.myLinks.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val stats = repository.getAdsterraStats()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Hero Wallet Card
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = SurfaceCardElevated,
                borderColor = PrimaryBlue.copy(alpha = 0.4f)
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBlue.copy(alpha = 0.2f))
                                    .border(1.5.dp, PrimaryBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentUser.username,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    if (currentUser.verified) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Verified",
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Level ${levelState.currentLevel} • ${levelState.tierName}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // VIP Badge
                        if (currentUser.isVip) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentGold.copy(alpha = 0.15f))
                                    .border(1.dp, AccentGold, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "VIP ACTIVE",
                                    color = AccentGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Credit Balance Big Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "CREDITS BALANCE",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = AccentGold,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format("%.1f", currentUser.credits),
                                    color = TextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Button(
                            onClick = { onNavigate(Screen.Shop.route) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Top Up",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // XP Progress
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "XP Progress: ${levelState.currentXp} / ${levelState.nextLevelXp}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "${(levelState.currentXp.toFloat() / levelState.nextLevelXp * 100).toInt()}%",
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { (levelState.currentXp.toFloat() / levelState.nextLevelXp).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = PrimaryBlue,
                            trackColor = SurfaceCardBorder,
                        )
                    }
                }
            }
        }

        // Live Direct Popup / Adsterra Metric Highlight
        item {
            NebulaCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.Adsterra.route) },
                backgroundColor = Color(0xFF151D2F),
                borderColor = PrimaryPurple.copy(alpha = 0.5f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = AccentGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ADSTERRA LIVE REVENUE",
                                color = AccentGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = "View Analytics ›",
                            color = PrimaryBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Today Revenue", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = "$${String.format("%.2f", stats.summary.todayRevenue)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text("Today CPM", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = "$${String.format("%.2f", stats.summary.todayCpm)}",
                                color = AccentGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text("7-Day Total", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = "$${String.format("%.2f", stats.summary.sevenDayRevenue)}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Action Grid
        item {
            Text(
                text = "Quick Actions",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickActionItem(
                    title = "Exchange Arena",
                    subtitle = "Earn Credits & XP",
                    icon = Icons.Default.Explore,
                    color = PrimaryBlue,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Discover.route) }
                )
                QuickActionItem(
                    title = "Create Campaign",
                    subtitle = "Drive Real Traffic",
                    icon = Icons.Default.AddCircle,
                    color = AccentGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.CreateLink.route) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickActionItem(
                    title = "Level Quests",
                    subtitle = "Daily Rewards",
                    icon = Icons.Default.MilitaryTech,
                    color = AccentGold,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Level.route) }
                )
                QuickActionItem(
                    title = "VIP Club",
                    subtitle = "1.6x Multipliers",
                    icon = Icons.Default.WorkspacePremium,
                    color = PrimaryPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Membership.route) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickActionItem(
                    title = "Referral Network",
                    subtitle = "10% Commission",
                    icon = Icons.Default.People,
                    color = AccentCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Referral.route) }
                )
                QuickActionItem(
                    title = "Leaderboards",
                    subtitle = "Top Earners",
                    icon = Icons.Default.EmojiEvents,
                    color = Color(0xFFFF7A00),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(Screen.Leaderboard.route) }
                )
            }
        }

        // Active Campaigns Preview
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Active Campaigns (${myLinks.size})",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Manage All ›",
                    color = PrimaryBlue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigate(Screen.MyLinks.route) }
                )
            }
        }

        items(myLinks.take(2)) { link ->
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = SurfaceCard
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (link.active) AccentGreen else AccentRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = link.title,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = link.url,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NebulaChip(text = "${link.todayVisits}/${link.dailyLimit} visits today", isSelected = false)
                            NebulaChip(text = "${link.creditsPerVisit} cr/visit", isSelected = true, color = AccentGold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
