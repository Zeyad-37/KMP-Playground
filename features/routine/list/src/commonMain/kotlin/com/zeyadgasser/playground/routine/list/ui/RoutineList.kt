package com.zeyadgasser.playground.routine.list.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
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
    date: String,
    categorizedRoutine: List<CategorisedRoutinePM>,
    listState: LazyListState,
    process: (RoutineListInput) -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        Text(date, Modifier.fillMaxWidth().padding(16.dp), color = Color.Gray, fontSize = 14.sp)
        LazyColumn(Modifier.fillMaxSize(), listState) {
            categorizedRoutine.forEach { categorizedRoutine ->
                stickyHeader {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = categorizedRoutine.category,
                            color = Color.DarkGray,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
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
}