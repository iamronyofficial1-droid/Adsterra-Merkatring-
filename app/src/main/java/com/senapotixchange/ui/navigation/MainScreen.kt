package com.senapotixchange.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.CreditsBalanceBadge
import com.senapotixchange.ui.screens.adsterra.AdsterraScreen
import com.senapotixchange.ui.screens.auth.AuthScreen
import com.senapotixchange.ui.screens.dashboard.DashboardScreen
import com.senapotixchange.ui.screens.leaderboard.LeaderboardScreen
import com.senapotixchange.ui.screens.level.LevelScreen
import com.senapotixchange.ui.screens.membership.MembershipScreen
import com.senapotixchange.ui.screens.mylinks.CreateCampaignDialog
import com.senapotixchange.ui.screens.mylinks.MyLinksScreen
import com.senapotixchange.ui.screens.notifications.NotificationScreen
import com.senapotixchange.ui.screens.profile.ProfileScreen
import com.senapotixchange.ui.screens.referral.ReferralScreen
import com.senapotixchange.ui.screens.settings.SettingsScreen
import com.senapotixchange.ui.screens.shop.ShopScreen
import com.senapotixchange.ui.screens.visit.VisitScreen
import com.senapotixchange.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    repository: ExchangeRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    val currentUser by repository.currentUser.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.read }

    Scaffold(
        topBar = {
            if (currentRoute != Screen.Auth.route) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AppGradients.Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapCalls,
                                    contentDescription = null,
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Senapoti Exchange",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Adsterra Marketing Hub",
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    },
                    actions = {
                        // Credits Pill
                        CreditsBalanceBadge(
                            credits = currentUser.credits,
                            onClick = { navController.navigate(Screen.Shop.route) }
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Notification Icon with Badge
                        IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                            BadgedBox(
                                badge = {
                                    if (unreadCount > 0) {
                                        Badge(
                                            containerColor = AccentRed,
                                            contentColor = Color.White
                                        ) {
                                            Text("$unreadCount")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = if (currentRoute == Screen.Notifications.route) PrimaryBlue else TextPrimary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BackgroundDark,
                        titleContentColor = TextPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (currentRoute != Screen.Auth.route) {
                NavigationBar(
                    containerColor = SurfaceDark,
                    tonalElevation = 8.dp,
                    modifier = Modifier.border(0.5.dp, SurfaceCardBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    BottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon ?: Icons.Default.Circle,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryBlue,
                                selectedTextColor = PrimaryBlue,
                                indicatorColor = PrimaryBlue.copy(alpha = 0.15f),
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        },
        containerColor = BackgroundDark
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Discover.route) {
                VisitScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.MyLinks.route) {
                MyLinksScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.CreateLink.route) {
                MyLinksScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Adsterra.route) {
                AdsterraScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Level.route) {
                LevelScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Membership.route) {
                MembershipScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Shop.route) {
                ShopScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Referral.route) {
                ReferralScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Notifications.route) {
                NotificationScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Profile.route) {
                ProfileScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Settings.route) {
                SettingsScreen(repository = repository, onNavigate = { navController.navigate(it) })
            }
            composable(Screen.Auth.route) {
                AuthScreen(onLoginSuccess = { navController.navigate(Screen.Dashboard.route) })
            }
        }
    }
}
