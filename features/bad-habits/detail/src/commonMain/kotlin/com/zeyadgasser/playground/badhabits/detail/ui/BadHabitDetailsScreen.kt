package com.zeyadgasser.playground.badhabits.detail.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState.InitialState
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailState.SuccessState
import com.zeyadgasser.playground.badhabits.detail.viewmodel.BadHabitDetailViewModel
import com.zeyadgasser.playground.badhabits.detail.viewmodel.DeleteBadHabitDetailInput
import com.zeyadgasser.playground.badhabits.detail.viewmodel.ErrorEffect
import com.zeyadgasser.playground.badhabits.detail.viewmodel.LoadBadHabitDetailInput
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavToEffect
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavigationInput.EditBadHabitDetailInput
import com.zeyadgasser.playground.badhabits.detail.viewmodel.NavigationInput.GoBackInput
import com.zeyadgasser.playground.badhabits.sharedpresentation.BadHabitPM
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.Bars.Data.Radius.Circular
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun BadHabitDetailsStateHolder(
    viewModel: BadHabitDetailViewModel = koinInject(),
    badHabitId: Long,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    val detailState by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is NavToEffect -> if (it.isUp) onDelete() else onEdit()
                is ErrorEffect -> coroutineScope.launch {
                    snackBarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
                }
            }
        }
    }
    BadHabitDetailsContent(detailState, badHabitId, snackBarHostState) { viewModel.process(it) }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun BadHabitDetailsContent(
    state: BadHabitDetailState,
    routineId: Long,
    snackBarHostState: SnackbarHostState,
    process: (Input) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
//                        text = stringResource(Res.string.routine_details),
                        text = "BadHabit Details",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { process(GoBackInput) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = stringResource(Res.string.back)
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { process(EditBadHabitDetailInput) }) {
//                        Text(text = stringResource(Res.string.edit), color = Color.Black)
                        Text(text = "Edit", color = Color.Black)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { Snackbar(it, contentColor = Color.Red) })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                InitialState -> process(LoadBadHabitDetailInput(routineId))
                is SuccessState -> {
                    BadHabitInfoSection(state.badHabit)
                    PerformanceSection(state.badHabit)
                    OptionsSection { process(it) }
                }
            }
        }
    }
}

@Composable
fun BadHabitInfoSection(state: SuccessState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Habit Title
        Text(
            text = "Late Night Snacking",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Habit Description
        Text(
            text = "Track your late-night snacking to improve your sleep and diet.",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        // Performance Section
        Text(
            text = "Performance",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Weekly Snacking
        Text(
            text = "Weekly Snacking",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        // Graph
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            val points = listOf(
                Offset(0f, 50f), Offset(50f, 70f), Offset(100f, 30f),
                Offset(150f, 60f), Offset(200f, 40f), Offset(250f, 80f),
                Offset(300f, 30f)
            )
            drawLine(
                color = Color.Gray,
                start = points.first(),
                end = points.last(),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        // Week Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Week 1")
            Text(text = "Week 2")
            Text(text = "Week 3")
            Text(text = "Week 4")
        }

        // Streaks Section
        Text(
            text = "Streaks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        // Current Streak and Longest Streak
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Current Streak", fontSize = 14.sp)
                    Text(text = "5 days", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Longest Streak", fontSize = 14.sp)
                    Text(text = "10 days", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
fun BadHabitInfoSection(badHabit: BadHabitPM) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = badHabit.name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badHabit.description,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PerformanceSection(badHabit: BadHabitPM) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Header
        Text(
//            text = stringResource(Res.string.performance),
            text = "Performance",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Subheader
        Text(
//            text = stringResource(Res.string.last_7_days),
            text = "Last 7 days",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Large Number
        Text(
            text = "80%",
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Small Text
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Avg. Completion ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "+10%",
                fontSize = 14.sp,
                color = Color.Green
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (badHabit.ratings.isNotEmpty())
            ColumnChart(
                modifier = Modifier.fillMaxWidth().padding(22.dp).height(300.dp),
                data = remember {
                    badHabit.ratings.map { // todo move logic to input handler
                        Bars(
                            label = it.date,
                            values = listOf(
                                Bars.Data(
                                    label = it.ratingValue.toString(),
                                    value = it.ratingValue.toDouble(),
                                    color = SolidColor(Color(0xFF23af92)),
                                )
                            )
                        )
                    }
                },
                barProperties = BarProperties(
                    spacing = 12.dp,
                    thickness = 37.dp,
                    cornerRadius = Circular(7.dp),
                ),
                animationMode = AnimationMode.Together(delayBuilder = { it * 200L }),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                minValue = 0.0,
            )
    }
}

@Composable
fun OptionsSection(process: (Input) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Header
        Text(
//            text = stringResource(Res.string.options),
            text = "Options",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Delete BadHabit Button
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .clickable { process(DeleteBadHabitDetailInput) }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
//                    painter = painterResource(Res.drawable.ic_delete),
//                    contentDescription = stringResource(Res.string.delete_routine),
                    contentDescription = "Delete BadHabit",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
//                    text = stringResource(Res.string.delete_routine),
                    text = "Delete BadHabit",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .clickable { process(EditBadHabitDetailInput) }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
//                    painter = painterResource(Res.drawable.ic_edit),
//                    contentDescription = stringResource(Res.string.edit),
                    contentDescription = "Edit",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
//                    text = stringResource(Res.string.edit),
                    text = "Edit",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}
