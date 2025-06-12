package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeyadgasser.playground.routine.list.viewmodel.CategorisedRoutinePM
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineCheckedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineClickedInput
import com.zeyadgasser.playground.routine.list.viewmodel.RoutineListInput

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoutineList(
    modifier: Modifier = Modifier,
    categorizedRoutine: List<CategorisedRoutinePM>,
    listState: LazyListState,
    process: (RoutineListInput) -> Unit,
) {
    Text("Today, July 24, 2024", Modifier.padding(32.dp, 0.dp))
    LazyColumn(modifier.fillMaxSize(), listState) {
        categorizedRoutine.forEach { categorizedRoutine ->
            stickyHeader {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        text = categorizedRoutine.category,
                        color = Color.DarkGray,
                        fontSize = 24.sp
                    )
                }
            }
            items(categorizedRoutine.routine, { item -> item.id }) { item ->
                RoutineItem(
                    routine = item,
                    onCheckChanged = { process(RoutineCheckedInput(item)) },
                    onClick = { process(RoutineClickedInput(item)) }
                )
            }
        }
    }
}
