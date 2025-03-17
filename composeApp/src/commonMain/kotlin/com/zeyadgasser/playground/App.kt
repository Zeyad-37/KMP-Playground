package com.zeyadgasser.playground

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.zeyadgasser.playground.sharedUI.theme.AppTheme
import com.zeyadgasser.playground.tasks.presentation.list.ui.ListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme { Navigator(ListScreen) }
}
