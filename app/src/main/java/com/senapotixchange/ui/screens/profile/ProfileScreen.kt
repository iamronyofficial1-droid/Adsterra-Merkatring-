package com.senapotixchange.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.navigation.Screen
import com.senapotixchange.ui.theme.*

@Composable
fun ProfileScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val levelState by repository.levelState.collectAsState()
    val myLinks by repository.myLinks.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Hero Profile Card
        item {
            NebulaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF161E33),
                borderColor = PrimaryBlue.copy(alpha = 0.5f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.2f))
                            .border(2.5.dp, PrimaryBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(42.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentUser.username,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (currentUser.verified) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = currentUser.email, color = TextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        NebulaChip(text = "Level ${levelState.currentLevel}", isSelected = true, color = PrimaryBlue)
                        NebulaChip(text = levelState.tierName, isSelected = true, color = AccentGold)
                        if (currentUser.isVip) {
                            NebulaChip(text = "VIP", isSelected = true, color = AccentGreen)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentUser.bio,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showEditDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardElevated),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.border(1.dp, SurfaceCardBorder, RoundedCornerShape(10.dp))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Profile", color = TextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }

        // Stats Summary Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetricBox(
                    title = "Total Campaigns",
                    value = "${myLinks.size}",
                    icon = Icons.Default.Link,
                    accentColor = PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )
                StatMetricBox(
                    title = "Visits Completed",
                    value = "${currentUser.totalVisits}",
                    icon = Icons.Default.DoneAll,
                    accentColor = AccentGreen,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMetricBox(
                    title = "Total Earnings",
                    value = "$${String.format("%.2f", currentUser.totalEarnings)}",
                    icon = Icons.Default.AttachMoney,
                    accentColor = AccentGold,
                    modifier = Modifier.weight(1f)
                )
                StatMetricBox(
                    title = "Daily Limit",
                    value = "${currentUser.dailyLimit}",
                    icon = Icons.Default.Speed,
                    accentColor = PrimaryPurple,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Account Menu List
        item {
            Text("Publisher Tools", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        item {
            NebulaCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    ProfileMenuItem(title = "VIP Membership Club", subtitle = "Upgrade multipliers & limits", icon = Icons.Default.WorkspacePremium, color = AccentGold) {
                        onNavigate(Screen.Membership.route)
                    }
                    Divider(color = SurfaceCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                    ProfileMenuItem(title = "Credits Top-Up Shop", subtitle = "Purchase campaign credits", icon = Icons.Default.ShoppingCart, color = PrimaryBlue) {
                        onNavigate(Screen.Shop.route)
                    }
                    Divider(color = SurfaceCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                    ProfileMenuItem(title = "Referral Network", subtitle = "Invite publishers & earn 10%", icon = Icons.Default.People, color = AccentCyan) {
                        onNavigate(Screen.Referral.route)
                    }
                    Divider(color = SurfaceCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                    ProfileMenuItem(title = "Level & Quests", subtitle = "Claim daily XP challenges", icon = Icons.Default.MilitaryTech, color = AccentGold) {
                        onNavigate(Screen.Level.route)
                    }
                    Divider(color = SurfaceCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                    ProfileMenuItem(title = "Account Settings", subtitle = "Safety, notifications, limits", icon = Icons.Default.Settings, color = TextSecondary) {
                        onNavigate(Screen.Settings.route)
                    }
                }
            }
        }
    }

    // Edit Profile Modal
    if (showEditDialog) {
        EditProfileDialog(
            currentUsername = currentUser.username,
            currentBio = currentUser.bio,
            onDismiss = { showEditDialog = false },
            onSave = { name, bio ->
                repository.updateProfile(name, bio)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(subtitle, color = TextSecondary, fontSize = 11.sp)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun EditProfileDialog(
    currentUsername: String,
    currentBio: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var username by remember { mutableStateOf(currentUsername) }
    var bio by remember { mutableStateOf(currentBio) }

    Dialog(onDismissRequest = onDismiss) {
        NebulaCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceDark,
            borderColor = PrimaryBlue
        ) {
            Column {
                Text("Edit Profile", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username", color = TextSecondary, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio", color = TextSecondary, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(username, bio) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Save Changes", color = TextPrimary)
                    }
                }
            }
        }
    }
}
