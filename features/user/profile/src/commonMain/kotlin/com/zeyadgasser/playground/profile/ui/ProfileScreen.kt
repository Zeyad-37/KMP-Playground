package com.zeyadgasser.playground.profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
//        Image(
//            painter = painterResource(Res.drawable.avatar_image), // Replace with your avatar image
//            contentDescription = "Avatar",
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//        )

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
            icon = Icons.Default.Settings,
            label = "Settings",
            onClick = { /* Handle Settings */ }
        )

        // Premium Features
        MenuItem(
            icon = Icons.Default.Star,
            label = "Premium Features",
            onClick = { /* Handle Premium Features */ }
        )

        // Sign Out
        MenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = "Sign Out",
            onClick = { /* Handle Sign Out */ }
        )
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
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
            imageVector = icon,
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