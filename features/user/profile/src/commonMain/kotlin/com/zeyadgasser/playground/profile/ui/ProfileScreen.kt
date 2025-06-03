package com.zeyadgasser.playground.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmpplayground.features.user.profile.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Header
        ProfileHeader()

        Spacer(modifier = Modifier.height(32.dp))

        // Menu Items
        ProfileMenuItems()
    }
}

@Composable
fun ProfileHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Avatar Image
        Image(
            painter = painterResource(Res.drawable.avatar_image), // Replace with your avatar image
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = "Sophia Carter",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Username
        Text(
            text = "@sophiacarter",
            color = Color.Blue,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ProfileMenuItems() {
    Column {
        // Settings
        MenuItem(
            icon = painterResource(Res.drawable.ic_settings),
            label = "Settings",
            onClick = { /* Handle Settings */ }
        )

        // Premium Features
        MenuItem(
            icon = painterResource(Res.drawable.ic_star),
            label = "Premium Features",
            onClick = { /* Handle Premium Features */ }
        )

        // Sign Out
        MenuItem(
            icon = painterResource(Res.drawable.ic_sign_out),
            label = "Sign Out",
            onClick = { /* Handle Sign Out */ }
        )
    }
}

@Composable
fun MenuItem(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}