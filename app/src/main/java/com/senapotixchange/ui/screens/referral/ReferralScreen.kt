package com.senapotixchange.ui.screens.referral

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*

@Composable
fun ReferralScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val referralData by repository.referralData.collectAsState()
    val context = LocalContext.current

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Referral Hero Banner
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF142033),
                borderColor = AccentCyan.copy(alpha = 0.5f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Referral Network", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text("Earn 10% lifetime credits on every referral visit", color = TextSecondary, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AccentCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Referral Link Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("YOUR UNIQUE INVITE LINK", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = referralData.referralLink,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                        IconButton(onClick = { copyToClipboard(referralData.referralLink, "Referral link") }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Code Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("INVITE CODE", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = referralData.referralCode,
                                color = AccentGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Button(
                            onClick = { copyToClipboard(referralData.referralCode, "Referral Code") },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Copy Code", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Referral Stats Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetricBox(
                    title = "Total Referrals",
                    value = "${referralData.totalReferrals}",
                    subtitle = "${referralData.activeReferrals} Active today",
                    icon = Icons.Default.People,
                    accentColor = PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )
                StatMetricBox(
                    title = "Earned Commissions",
                    value = "${referralData.totalEarnedCredits.toInt()} Cr",
                    subtitle = "10% Commission Rate",
                    icon = Icons.Default.MonetizationOn,
                    accentColor = AccentGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Referred Publishers List
        item {
            Text("Referred Publishers (${referralData.referredUsers.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        items(referralData.referredUsers) { refUser ->
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = SurfaceCard
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceCardElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(refUser.username, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (refUser.active) AccentGreen else TextMuted)
                                )
                            }
                            Text("Joined: ${refUser.joinedDate} • ${refUser.visitsCount} visits", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("+${String.format("%.1f", refUser.commissionEarned)} Cr", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Earned", color = TextSecondary, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
