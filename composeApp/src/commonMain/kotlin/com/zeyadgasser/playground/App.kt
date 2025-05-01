package com.zeyadgasser.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.zeyadgasser.playground.breath.ui.BreathingCoachAppScreen
import com.zeyadgasser.playground.sharedui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(modifier: Modifier) {
//    AppTheme { Navigator(ListScreen(modifier)) }
    AppTheme { Navigator(BreathingCoachAppScreen(modifier)) }
}
