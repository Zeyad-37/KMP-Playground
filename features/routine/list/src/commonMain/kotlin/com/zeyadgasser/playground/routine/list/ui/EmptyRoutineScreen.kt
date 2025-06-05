package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeyadgasser.playground.routine.list.viewmodel.CreateRoutineInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListInput
import com.zeyadgasser.playground.routine.list.resources.Res
import com.zeyadgasser.playground.routine.list.resources.add_icon
import com.zeyadgasser.playground.routine.list.resources.create_routine
import com.zeyadgasser.playground.routine.list.resources.create_your_first_routine_motivation_label
import com.zeyadgasser.playground.routine.list.resources.no_routines_yet
import com.zeyadgasser.playground.routine.list.resources.routine_image_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyRoutineScreen(modifier: Modifier = Modifier, process: (RoutineListInput) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Image Section
        Image(
            painter = painterResource(Res.drawable.add_icon),
            contentDescription = stringResource(Res.string.routine_image_description),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.no_routines_yet),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.create_your_first_routine_motivation_label),
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button Section
        Button(
            onClick = { process(CreateRoutineInput) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = stringResource(Res.string.create_routine), fontSize = 16.sp)
        }
    }
}
