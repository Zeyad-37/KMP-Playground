package com.zeyadgasser.playground.sharedUI.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    h1 = AppTypography.Headline,
    h2 = AppTypography.Headline,
    h3 = AppTypography.Headline,
    h4 = AppTypography.Headline,
    h5 = AppTypography.Headline,
    body1 = AppTypography.Body1Medium,
    body2 = AppTypography.Body2Medium,
    subtitle1 = AppTypography.Body1Regular,
    subtitle2 = AppTypography.Body2Regular,
//    displayLarge = AppTypography.Headline,
//    bodyLarge = AppTypography.Body1Medium,
//    bodyMedium = AppTypography.Body2Medium,
//    bodySmall = AppTypography.Body1Regular,
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
