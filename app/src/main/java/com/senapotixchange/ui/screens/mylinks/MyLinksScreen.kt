package com.senapotixchange.ui.screens.mylinks

import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.senapotixchange.data.model.CreateLinkRequest
import com.senapotixchange.data.model.LinkDto
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.navigation.Screen
import com.senapotixchange.ui.theme.*

@Composable
fun MyLinksScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val myLinks by repository.myLinks.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var linkToDelete by remember { mutableStateOf<LinkDto?>(null) }

    val totalVisits = myLinks.sumOf { it.totalVisits }
    val todayVisits = myLinks.sumOf { it.todayVisits }
    val activeCount = myLinks.count { it.active }

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
            // Header Stats Overview
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
                            Text(
                                text = "Campaign Manager",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = { showCreateDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Campaign", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Active Links", color = TextSecondary, fontSize = 11.sp)
                                Text("$activeCount / ${myLinks.size}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Column {
                                Text("Today Visits", color = TextSecondary, fontSize = 11.sp)
                                Text("$todayVisits", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Column {
                                Text("All-Time Visits", color = TextSecondary, fontSize = 11.sp)
                                Text("$totalVisits", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // Campaigns List Header
            item {
                Text(
                    text = "My Active Campaigns (${myLinks.size})",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (myLinks.isEmpty()) {
                item {
                    NebulaCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = SurfaceCard
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.LinkOff, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No campaigns yet", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Create your first campaign to start receiving targeted traffic.", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showCreateDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                            ) {
                                Text("Create Campaign", color = TextPrimary)
                            }
                        }
                    }
                }
            }

            items(myLinks, key = { it.id }) { link ->
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
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (link.active) AccentGreen else AccentRed)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = link.title,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = link.url,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    NebulaChip(text = link.category, isSelected = false)
                                    if (link.isAdsterra) {
                                        NebulaChip(text = "Adsterra Smartlink", isSelected = true, color = PrimaryPurple)
                                    }
                                }
                            }

                            // Switch
                            Switch(
                                checked = link.active,
                                onCheckedChange = { repository.toggleLinkActive(link.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AccentGreen,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = SurfaceCardBorder
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = SurfaceCardBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats metrics per campaign
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Cost / Visit", color = TextSecondary, fontSize = 10.sp)
                                Text("${link.creditsPerVisit} Cr", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Column {
                                Text("Daily Limit", color = TextSecondary, fontSize = 10.sp)
                                Text("${link.todayVisits}/${link.dailyLimit}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Column {
                                Text("Total Visits", color = TextSecondary, fontSize = 10.sp)
                                Text("${link.totalVisits}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            IconButton(
                                onClick = { linkToDelete = link },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = AccentRed, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Create Campaign Dialog
        if (showCreateDialog) {
            CreateCampaignDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { req ->
                    repository.createLink(req)
                    showCreateDialog = false
                }
            )
        }

        // Delete Confirmation Dialog
        if (linkToDelete != null) {
            AlertDialog(
                onDismissRequest = { linkToDelete = null },
                title = { Text("Delete Campaign?", color = TextPrimary, fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to delete '${linkToDelete?.title}'? Remaining credits will be returned to your balance.", color = TextSecondary) },
                confirmButton = {
                    Button(
                        onClick = {
                            linkToDelete?.let { repository.deleteLink(it.id) }
                            linkToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { linkToDelete = null }) {
                        Text("Cancel", color = TextSecondary)
                    }
                },
                containerColor = SurfaceDark,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun CreateCampaignDialog(
    onDismiss: () -> Unit,
    onCreate: (CreateLinkRequest) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("https://") }
    var category by remember { mutableStateOf("Adsterra Direct Link") }
    var creditsPerVisit by remember { mutableFloatStateOf(5.0f) }
    var dailyLimit by remember { mutableFloatStateOf(200f) }
    var isAdsterra by remember { mutableStateOf(true) }

    val categories = listOf("Adsterra Direct Link", "Smartlink", "Finance", "Tech", "General")

    Dialog(onDismissRequest = onDismiss) {
        NebulaCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceDark,
            borderColor = PrimaryBlue
        ) {
            Column {
                Text(
                    text = "Launch New Campaign",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Campaign Title", color = TextSecondary, fontSize = 12.sp) },
                    placeholder = { Text("e.g. High CPM Direct Link", color = TextMuted) },
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
                Spacer(modifier = Modifier.height(10.dp))

                // URL Input
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Destination URL / Smartlink", color = TextSecondary, fontSize = 12.sp) },
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
                Spacer(modifier = Modifier.height(12.dp))

                // Category Selection
                Text("Category", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.take(3).forEach { cat ->
                        NebulaChip(
                            text = if (cat.contains("Adsterra")) "Adsterra" else cat,
                            isSelected = category == cat,
                            onClick = {
                                category = cat
                                isAdsterra = cat.contains("Adsterra")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Credits Per Visit Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Credits / Visit:", color = TextSecondary, fontSize = 12.sp)
                    Text("${String.format("%.1f", creditsPerVisit)} Credits", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Slider(
                    value = creditsPerVisit,
                    onValueChange = { creditsPerVisit = it },
                    valueRange = 2.0f..15.0f,
                    steps = 12,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentGold,
                        activeTrackColor = AccentGold,
                        inactiveTrackColor = SurfaceCardBorder
                    )
                )

                // Daily Limit Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Daily Visits Cap:", color = TextSecondary, fontSize = 12.sp)
                    Text("${dailyLimit.toInt()} visits/day", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Slider(
                    value = dailyLimit,
                    onValueChange = { dailyLimit = it },
                    valueRange = 50f..1000f,
                    steps = 18,
                    colors = SliderDefaults.colors(
                        thumbColor = PrimaryBlue,
                        activeTrackColor = PrimaryBlue,
                        inactiveTrackColor = SurfaceCardBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && url.isNotBlank()) {
                                onCreate(
                                    CreateLinkRequest(
                                        title = title,
                                        url = url,
                                        category = category,
                                        creditsPerVisit = creditsPerVisit.toDouble(),
                                        dailyLimit = dailyLimit.toInt(),
                                        isAdsterra = isAdsterra
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Launch Campaign", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
