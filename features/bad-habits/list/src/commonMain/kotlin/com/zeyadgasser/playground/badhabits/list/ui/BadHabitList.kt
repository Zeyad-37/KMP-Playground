package com.zeyadgasser.playground.badhabits.list.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitListInput
import com.zeyadgasser.playground.badhabits.list.viewmodel.BadHabitRatedInput
import com.zeyadgasser.playground.badhabits.list.viewmodel.NavigationInput.BadHabitClickedInput
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitPM

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BadHabitList(
    modifier: Modifier = Modifier,
    date: String,
    badHabits: List<BadHabitPM>,
    listState: LazyListState,
    process: (BadHabitListInput) -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        Text(date, Modifier.fillMaxWidth().padding(16.dp), color = Color.Gray, fontSize = 14.sp)
        LazyColumn(Modifier.fillMaxSize().padding(16.dp), listState) {
            items(badHabits, { item -> item.id }) { item ->
                BadHabitItem(
                    name = item.name,
                    frequency = item.frequency,
                    currentCount = item.currentRating.toString(),
                    onClick = { process(BadHabitClickedInput(item)) },
                    onCountChanged = { process(BadHabitRatedInput(item, it)) }
                )
            }
        }
    }
}
