package com.senapotixchange.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Obsidian & Nebula Palette
val BackgroundDark = Color(0xFF0A0D14)
val SurfaceDark = Color(0xFF131824)
val SurfaceCard = Color(0xFF181F30)
val SurfaceCardBorder = Color(0xFF26334D)
val SurfaceCardElevated = Color(0xFF1E273D)

// Brand Primary Gradients
val PrimaryBlue = Color(0xFF4F7FFF)
val PrimaryPurple = Color(0xFF6C63FF)
val PrimaryViolet = Color(0xFF7C5CFC)
val SecondaryTeal = Color(0xFF00D2FF)

// Functional Accents
val AccentGold = Color(0xFFFFB800)
val AccentGoldGlow = Color(0x33FFB800)
val AccentGreen = Color(0xFF10B981)
val AccentGreenGlow = Color(0x3310B981)
val AccentRed = Color(0xFFEF4444)
val AccentRedGlow = Color(0x33EF4444)
val AccentCyan = Color(0xFF06B6D4)

// Text & Neutral Shades
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)
val TextMuted = Color(0xFF475569)

// Gradients
object AppGradients {
    val Primary = Brush.horizontalGradient(
        colors = listOf(PrimaryBlue, PrimaryPurple, PrimaryViolet)
    )
    val PrimaryVertical = Brush.verticalGradient(
        colors = listOf(PrimaryBlue, PrimaryPurple)
    )
    val Gold = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFFB800), Color(0xFFFF8A00))
    )
    val Green = Brush.horizontalGradient(
        colors = listOf(Color(0xFF10B981), Color(0xFF059669))
    )
    val CardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF182032), Color(0xFF111724))
    )
    val CardGlowBlue = Brush.verticalGradient(
        colors = listOf(Color(0xFF1B2A4A), Color(0xFF111724))
    )
}
