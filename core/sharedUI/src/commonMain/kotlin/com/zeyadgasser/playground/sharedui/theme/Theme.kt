package com.zeyadgasser.playground.sharedui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content,
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = AppColors.Primary,
            onPrimary = AppColors.OnPrimary,
            background = AppColors.Background,
            onBackground = AppColors.OnBackground,
            surface = AppColors.Surface
        ),
        typography = Typography,
    )
}
