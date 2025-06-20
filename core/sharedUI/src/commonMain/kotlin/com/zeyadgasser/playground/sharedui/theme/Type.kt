package com.zeyadgasser.playground.sharedui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography( // TODO Review
    displayLarge = AppTypography.Headline,
    displayMedium = AppTypography.Body2Medium,
    displaySmall = AppTypography.Body2Regular,
    headlineLarge = AppTypography.Headline,
    headlineMedium = AppTypography.Headline,
    headlineSmall = AppTypography.Headline,
    titleLarge = AppTypography.Headline,
    titleMedium = AppTypography.Headline,
    titleSmall = AppTypography.Headline,
    bodyLarge = AppTypography.Body1Medium,
    bodyMedium = AppTypography.Body2Medium,
    bodySmall = AppTypography.Body1Regular,
    labelLarge = AppTypography.Body1Medium,
    labelMedium = AppTypography.Body1Medium,
    labelSmall = AppTypography.Body1Medium,
)

object AppTypography {
    val Headline = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        fontWeight = FontWeight.Bold
    )

    val Body1Medium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Medium
    )

    val Body1Regular = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Normal
    )

    val Body2Medium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Medium
    )

    val Body2Regular = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Normal
    )
}
