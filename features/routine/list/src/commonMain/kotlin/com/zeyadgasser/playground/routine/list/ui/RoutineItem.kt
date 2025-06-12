package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RoutineItem(
    routine: RoutinePM,
    onCheckChanged: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp, 16.dp, 32.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            AsyncImage(
//                model = routine.image,
//                contentScale = ContentScale.FillWidth,
//                contentDescription = null,
//                clipToBounds = true,
////                error = routine.icon?.let { painterResource(it) },
////                placeholder = routine.icon?.let { painterResource(it) },
//            )
//            Icon(painterResource(routine.icon!!), "routine icon")
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(routine.name)
                Text("${routine.startTime} - ${routine.endTime}")
            }
            Checkbox(
                checked = routine.completed,
                onCheckedChange = onCheckChanged
            )
        }
    }
}

@Preview
@Composable
fun PreviewRoutineItem() {
    RoutineItem(
        routine = RoutinePM(
            id = 1,
            name = "Morning Stretch",
            type = "MORNING",
            startTime = "01:03",
            endTime = "01:24",
            description = "Stretch body after waking up.",
            completed = false,
            category = "Morning"
        ),
        onCheckChanged = {},
        onClick = {}
    )
}
