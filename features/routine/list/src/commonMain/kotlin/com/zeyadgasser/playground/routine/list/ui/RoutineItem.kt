package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zeyadgasser.playground.routine.list.resources.Res
import com.zeyadgasser.playground.routine.list.resources.ic_sunny
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RoutineItem(
    routine: RoutinePM,
    onCheckChanged: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    var isChecked by remember { mutableStateOf(routine.completed) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
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
            Surface(color = Color(0xFFF2F2F5), shape = RoundedCornerShape(8.dp)) {
                Icon(painterResource(Res.drawable.ic_sunny), "routine icon", modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly) {
                Text(routine.name)
                Text("${routine.startTime} - ${routine.endTime}", color = Color.Gray)
            }
            Checkbox(
                modifier = Modifier.clickable {
                    isChecked = !isChecked
                    onCheckChanged(isChecked)
                },
                checked = isChecked,
                onCheckedChange = { onCheckChanged(it) }
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
