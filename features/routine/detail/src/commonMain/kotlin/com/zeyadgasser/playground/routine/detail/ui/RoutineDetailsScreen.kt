package com.zeyadgasser.playground.routine.detail.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.routine.detail.viewmodel.DeleteRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.ErrorEffect
import com.zeyadgasser.playground.routine.detail.viewmodel.LoadRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.NavToEffect
import com.zeyadgasser.playground.routine.detail.viewmodel.NavigationInput.EditRoutineDetailInput
import com.zeyadgasser.playground.routine.detail.viewmodel.NavigationInput.GoBackInput
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState.InitialState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailState.SuccessState
import com.zeyadgasser.playground.routine.detail.viewmodel.RoutineDetailViewModel
import com.zeyadgasser.playground.routine.sharedpresentation.RoutinePM
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.Bars.Data.Radius.Circular
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun RoutineDetailsStateHolder(
    viewModel: RoutineDetailViewModel = koinInject(),
    routineId: Long,
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
    RoutineDetailsContent(detailState, routineId, snackBarHostState) { viewModel.process(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailsContent(
    state: RoutineDetailState,
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
                        text = "Routine Details",
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
                    IconButton(onClick = { process(EditRoutineDetailInput) }) {
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
                InitialState -> process(LoadRoutineDetailInput(routineId))
                is SuccessState -> {
                    RoutineInfoSection(state.routine)
                    PerformanceSection(state.routine)
                    OptionsSection { process(it) }
                }
            }
        }
    }
}

@Composable
fun RoutineInfoSection(routine: RoutinePM) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = routine.name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = routine.description,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PerformanceSection(routine: RoutinePM) {
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
        if (routine.ratings.isNotEmpty())
            ColumnChart(
                modifier = Modifier.fillMaxWidth().padding(22.dp).height(300.dp),
                data = remember {
                    routine.ratings.map { // todo move logic to input handler
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
                maxValue = 5.0,
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

        // Delete Routine Button
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .clickable { process(DeleteRoutineDetailInput) }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
//                    painter = painterResource(Res.drawable.ic_delete),
//                    contentDescription = stringResource(Res.string.delete_routine),
                    contentDescription = "Delete Routine",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
//                    text = stringResource(Res.string.delete_routine),
                    text = "Delete Routine",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .clickable { process(EditRoutineDetailInput) }
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

@Preview
@Composable
fun PreviewRoutineDetailsScreen() {
//    RoutineDetailsScreen()
}
