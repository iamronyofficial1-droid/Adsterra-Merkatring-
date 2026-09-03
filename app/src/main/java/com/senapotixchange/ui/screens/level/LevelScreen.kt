package com.senapotixchange.ui.screens.level

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
import com.senapotixchange.data.model.XpTask
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*

@Composable
fun LevelScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val levelState by repository.levelState.collectAsState()
    val currentUser by repository.currentUser.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Level Hero Card
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF192036),
                borderColor = PrimaryBlue.copy(alpha = 0.5f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.15f))
                            .border(2.5.dp, PrimaryBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = AccentGold, modifier = Modifier.size(40.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Level ${levelState.currentLevel} • ${levelState.tierName}",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${levelState.tierMultiplier}x Multiplier Active on All Visits",
                        color = AccentGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${levelState.currentXp} XP", color = TextSecondary, fontSize = 12.sp)
                        Text("${levelState.nextLevelXp} XP Next Tier", color = TextSecondary, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (levelState.currentXp.toFloat() / levelState.nextLevelXp).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PrimaryBlue,
                        trackColor = SurfaceCardBorder
                    )
                }
            }
        }

        // Tier Benefits Checklist
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = SurfaceCard
            ) {
                Column {
                    Text("Current Tier Benefits", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    TierBenefitItem(text = "${levelState.tierMultiplier}x Base exchange rewards bonus", active = true)
                    TierBenefitItem(text = "Priority discovery rotation in Exchange Arena", active = true)
                    TierBenefitItem(text = "Max daily limit unlocked (150 visits/day)", active = true)
                    TierBenefitItem(text = "Adsterra Smartlink CPM booster algorithm", active = true)
                }
            }
        }

        // Quests & Tasks Header
        item {
            Text(
                text = "Quests & Challenges",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        items(levelState.tasks, key = { it.id }) { task ->
            TaskItemCard(
                task = task,
                onClaim = { repository.claimTaskReward(task.id) }
            )
        }
    }
}

@Composable
fun TierBenefitItem(text: String, active: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (active) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (active) AccentGreen else TextMuted,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = if (active) TextPrimary else TextSecondary, fontSize = 13.sp)
    }
}

@Composable
fun TaskItemCard(
    task: XpTask,
    onClaim: () -> Unit
) {
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
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (task.category) {
                                        "Daily" -> PrimaryBlue.copy(alpha = 0.2f)
                                        "Weekly" -> PrimaryPurple.copy(alpha = 0.2f)
                                        else -> AccentGold.copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task.category.uppercase(),
                                color = when (task.category) {
                                    "Daily" -> PrimaryBlue
                                    "Weekly" -> PrimaryPurple
                                    else -> AccentGold
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = task.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = task.description, color = TextSecondary, fontSize = 12.sp)
                }

                // Reward Tag
                Column(horizontalAlignment = Alignment.End) {
                    Text("+${task.creditReward.toInt()} Cr", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("+${task.xpReward} XP", color = PrimaryBlue, fontWeight = FontWeight.Medium, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress + Claim Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${task.currentProgress} / ${task.targetProgress}", color = TextSecondary, fontSize = 11.sp)
                        Text(
                            "${((task.currentProgress.toFloat() / task.targetProgress).coerceAtMost(1f) * 100).toInt()}%",
                            color = if (task.completed) AccentGreen else PrimaryBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (task.currentProgress.toFloat() / task.targetProgress).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = if (task.completed) AccentGreen else PrimaryBlue,
                        trackColor = SurfaceCardBorder
                    )
                }

                if (task.claimed) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceCardBorder)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Claimed", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onClaim,
                        enabled = task.completed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen,
                            disabledContainerColor = SurfaceCardBorder
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (task.completed) "Claim Reward" else "In Progress",
                            color = if (task.completed) Color.Black else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
