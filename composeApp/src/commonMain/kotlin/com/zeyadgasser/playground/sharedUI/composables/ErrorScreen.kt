package com.zeyadgasser.playground.sharedUI.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.sharedUI.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorScreen(message: String) {
    Text(text = message, Modifier.padding(24.dp), color = Red)
}

@Composable
@Preview
fun ErrorScreenPreview() {
    AppTheme {
        ErrorScreen("Error message")
    }
}
