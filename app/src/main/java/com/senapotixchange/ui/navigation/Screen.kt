package com.senapotixchange.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Discover : Screen("discover", "Exchange Arena", Icons.Default.Explore)
    object Visit : Screen("visit", "Visit Link", Icons.Default.OpenInBrowser)
    object MyLinks : Screen("my_links", "My Campaigns", Icons.Default.Link)
    object CreateLink : Screen("create_link", "Create Campaign", Icons.Default.AddLink)
    object Adsterra : Screen("adsterra", "Adsterra Analytics", Icons.Default.TrendingUp)
    object Leaderboard : Screen("leaderboard", "Leaderboard", Icons.Default.EmojiEvents)
    object Level : Screen("level", "Level & Quests", Icons.Default.MilitaryTech)
    object Membership : Screen("membership", "VIP Club", Icons.Default.WorkspacePremium)
    object Shop : Screen("shop", "Credits Shop", Icons.Default.ShoppingCart)
    object Referral : Screen("referral", "Referral Network", Icons.Default.People)
    object Notifications : Screen("notifications", "Notifications", Icons.Default.Notifications)
    object Profile : Screen("profile", "Publisher Profile", Icons.Default.Person)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Auth : Screen("auth", "Sign In", Icons.Default.Lock)
}

val BottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Discover,
    Screen.MyLinks,
    Screen.Adsterra,
    Screen.Profile
)
