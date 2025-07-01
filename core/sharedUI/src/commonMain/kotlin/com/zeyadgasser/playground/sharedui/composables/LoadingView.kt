package com.zeyadgasser.playground.sharedui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView(modifier: Modifier) {
    Column(
        modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier.requiredSize(52.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}