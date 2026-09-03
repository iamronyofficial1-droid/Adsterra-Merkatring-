package com.senapotixchange.ui.screens.notifications

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senapotixchange.data.model.NotificationDto
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.NebulaCard
import com.senapotixchange.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val notifications by repository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.read }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Header
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF151D2E),
                borderColor = PrimaryBlue.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Notifications", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("$unreadCount unread updates", color = TextSecondary, fontSize = 12.sp)
                    }

                    if (unreadCount > 0) {
                        TextButton(onClick = { repository.markAllNotificationsAsRead() }) {
                            Text("Mark all as read", color = PrimaryBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (notifications.isEmpty()) {
            item {
                NebulaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No notifications", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("You're all caught up!", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }

        items(notifications, key = { it.id }) { notif ->
            val icon = when (notif.type) {
                "REWARD" -> Icons.Default.MonetizationOn
                "LEVEL_UP" -> Icons.Default.MilitaryTech
                "REFERRAL" -> Icons.Default.People
                "CAMPAIGN" -> Icons.Default.TrendingUp
                else -> Icons.Default.Notifications
            }
            val accentColor = when (notif.type) {
                "REWARD" -> AccentGold
                "LEVEL_UP" -> PrimaryBlue
                "REFERRAL" -> AccentCyan
                "CAMPAIGN" -> AccentGreen
                else -> PrimaryPurple
            }

            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (!notif.read) SurfaceCardElevated else SurfaceCard,
                borderColor = if (!notif.read) accentColor.copy(alpha = 0.5f) else SurfaceCardBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(notif.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            if (!notif.read) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlue)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(notif.message, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(notif.timestamp)),
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
