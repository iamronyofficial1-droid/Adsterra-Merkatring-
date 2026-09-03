package com.senapotixchange.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.senapotixchange.data.model.AdsterraLeaderboardEntry
import com.senapotixchange.data.model.LeaderboardEntry
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*

@Composable
fun LeaderboardScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val communityList = repository.getCommunityLeaderboard()
    val adsterraList = repository.getAdsterraLeaderboard()

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
            // Header
            item {
                NebulaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF161C2C),
                    borderColor = AccentGold.copy(alpha = 0.5f)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Leaderboards", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text("Top ranked publishers & traffic champions", color = TextSecondary, fontSize = 12.sp)
                            }
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = AccentGold, modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Tab selector
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SurfaceDark)
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedTab == 0) PrimaryBlue else Color.Transparent)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Exchange Top",
                                    color = if (selectedTab == 0) TextPrimary else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedTab == 1) PrimaryPurple else Color.Transparent)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Adsterra Kings",
                                    color = if (selectedTab == 1) TextPrimary else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Top 3 Podium
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (selectedTab == 0 && communityList.size >= 3) {
                        PodiumCard(rank = 2, name = communityList[1].username, score = "${communityList[1].score.toInt()} Cr", color = Color(0xFFC0C0C0), height = 120)
                        PodiumCard(rank = 1, name = communityList[0].username, score = "${communityList[0].score.toInt()} Cr", color = AccentGold, height = 145)
                        PodiumCard(rank = 3, name = communityList[2].username, score = "${communityList[2].score.toInt()} Cr", color = Color(0xFFCD7F32), height = 105)
                    } else if (selectedTab == 1 && adsterraList.size >= 3) {
                        PodiumCard(rank = 2, name = adsterraList[1].username, score = "$${adsterraList[1].revenue.toInt()}", color = Color(0xFFC0C0C0), height = 120)
                        PodiumCard(rank = 1, name = adsterraList[0].username, score = "$${adsterraList[0].revenue.toInt()}", color = AccentGold, height = 145)
                        PodiumCard(rank = 3, name = adsterraList[2].username, score = "$${adsterraList[2].revenue.toInt()}", color = Color(0xFFCD7F32), height = 105)
                    }
                }
            }

            // Ranked List
            if (selectedTab == 0) {
                items(communityList) { entry ->
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = if (entry.username.contains("(You)")) SurfaceCardElevated else SurfaceCard,
                        borderColor = if (entry.username.contains("(You)")) AccentGold else SurfaceCardBorder
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceDark),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${entry.rank}",
                                        color = if (entry.rank <= 3) AccentGold else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = entry.username, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        if (entry.verified) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                    Text(text = "Level ${entry.level} • ${entry.badge}", color = TextSecondary, fontSize = 11.sp)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${entry.score.toInt()} Cr",
                                    color = AccentGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Score",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            } else {
                items(adsterraList) { entry ->
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = if (entry.username.contains("(You)")) SurfaceCardElevated else SurfaceCard,
                        borderColor = if (entry.username.contains("(You)")) AccentGreen else SurfaceCardBorder
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceDark),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${entry.rank}",
                                        color = if (entry.rank <= 3) AccentGreen else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = entry.username, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        if (entry.verified) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                    Text(text = "${entry.impressions} Imp • ${entry.clicks} Clicks", color = TextSecondary, fontSize = 11.sp)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$${String.format("%.2f", entry.revenue)}",
                                    color = AccentGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Revenue",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumCard(
    rank: Int,
    name: String,
    score: String,
    color: Color,
    height: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "#$rank", color = color, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = name, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(text = score, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(SurfaceCard)
                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = color.copy(alpha = 0.6f), modifier = Modifier.size(28.dp))
        }
    }
}
