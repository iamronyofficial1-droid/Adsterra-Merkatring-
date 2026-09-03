package com.senapotixchange.ui.screens.membership

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
import com.senapotixchange.data.model.MembershipPlanDto
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*

@Composable
fun MembershipScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val membershipStatus by repository.membershipStatus.collectAsState()
    val plans = repository.getMembershipPlans()
    var showSuccessMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Current Status Card
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1B1A33),
                borderColor = AccentGold.copy(alpha = 0.6f)
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(AccentGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Active Membership", color = TextSecondary, fontSize = 12.sp)
                                Text(membershipStatus.currentPlan, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AccentGold)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${membershipStatus.daysRemaining} Days Left",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Your account is receiving ${membershipStatus.visitMultiplier}x exchange bonus credits and prioritized link rotation across the global network.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item {
            Text("Select Upgrade Plan", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        items(plans) { plan ->
            val isCurrentPlan = membershipStatus.currentPlan == plan.name
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (isCurrentPlan) SurfaceCardElevated else SurfaceCard,
                borderColor = if (isCurrentPlan) AccentGold else SurfaceCardBorder
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(plan.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                if (isCurrentPlan) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(AccentGold.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("CURRENT", color = AccentGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${plan.visitMultiplier}x Credits Multiplier", color = PrimaryBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (plan.price == 0.0) "Free" else "$${String.format("%.2f", plan.price)}",
                                color = AccentGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                            if (plan.price > 0.0) {
                                Text("/ month", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = SurfaceCardBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    plan.features.forEach { feat ->
                        Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(feat, color = TextSecondary, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            repository.upgradeMembership(plan)
                            showSuccessMessage = "Successfully upgraded to ${plan.name}!"
                        },
                        enabled = !isCurrentPlan,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (plan.name.contains("Elite")) PrimaryPurple else PrimaryBlue,
                            disabledContainerColor = SurfaceCardBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text(
                            text = if (isCurrentPlan) "Active Plan" else "Upgrade to ${plan.name}",
                            color = if (isCurrentPlan) TextMuted else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
