package com.zeyadgasser.playground.routine.form.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.routine.form.viewmodel.CloseCreateRoutineEffect
import com.zeyadgasser.playground.routine.form.viewmodel.CloseRoutineFormInput
import com.zeyadgasser.playground.routine.form.viewmodel.HideTimePickerEffect
import com.zeyadgasser.playground.routine.form.viewmodel.LoadRoutineByIdInput
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineForm
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.InitialState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ReadyToSubmitState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ValidationErrorState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormViewModel
import com.zeyadgasser.playground.routine.form.viewmodel.ShowTimePickerEffect
import com.zeyadgasser.playground.routine.form.viewmodel.ShowTimePickerInput
import com.zeyadgasser.playground.routine.form.viewmodel.SubmitRoutineInput
import com.zeyadgasser.playground.routine.form.viewmodel.ValidateFormInput
import kmpplayground.features.routine.form.generated.resources.Res
import kmpplayground.features.routine.form.generated.resources.close
import kmpplayground.features.routine.form.generated.resources.create_routine
import kmpplayground.features.routine.form.generated.resources.description
import kmpplayground.features.routine.form.generated.resources.end_time
import kmpplayground.features.routine.form.generated.resources.ic_close
import kmpplayground.features.routine.form.generated.resources.morning_run
import kmpplayground.features.routine.form.generated.resources.new_routine
import kmpplayground.features.routine.form.generated.resources.reminders
import kmpplayground.features.routine.form.generated.resources.routine_category
import kmpplayground.features.routine.form.generated.resources.routine_name
import kmpplayground.features.routine.form.generated.resources.routine_type
import kmpplayground.features.routine.form.generated.resources.start_time
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun RoutineFormScreenStateHolder(
    viewModel: RoutineFormViewModel = koinInject(),
    routineId: Long? = null,
    onCloseFormClick: () -> Unit,
) {
    val routinesState by viewModel.state.collectAsStateWithLifecycle()
    var showStartDialog by remember { mutableStateOf(false) }
    var showEndDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                CloseCreateRoutineEffect -> onCloseFormClick()
                is ShowTimePickerEffect -> if (it.isStart) showStartDialog = true else showEndDialog = true
                HideTimePickerEffect -> {
                    showStartDialog = false
                    showEndDialog = false
                }
            }
        }
    }
    RoutineFormScreenContent(
        routinesState,
        showStartDialog,
        showEndDialog,
        routineId
    ) { viewModel.process(it) }
}

@Composable
fun RoutineFormScreenContent(
    state: RoutineFormState,
    showStartTimePicker: Boolean,
    showEndTimePicker: Boolean,
    routineId: Long? = null,
    process: (Input) -> Unit,
) {
    var routineName by remember { mutableStateOf(state.form.routineName) }
    var isRoutineNameError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.routineNameValidationErrorMessage != null)
    }
    var routineNameErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.routineNameValidationErrorMessage.orEmpty())
    }
    var routineType by remember { mutableStateOf(state.form.routineType) }
    var isRoutineTypeError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.routineTypeValidationErrorMessage != null)
    }
    var routineTypeErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.routineTypeValidationErrorMessage.orEmpty())
    }
    var routineCategory by remember { mutableStateOf(state.form.routineCategory) }
    var isRoutineCategoryError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.routineCategoryValidationErrorMessage != null)
    }
    var routineCategoryErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.routineCategoryValidationErrorMessage.orEmpty())
    }
    var startTime by remember { mutableStateOf(state.form.startTime) }
//    var isRoutineStartTimeError by remember {
//        mutableStateOf(
//            state is ValidationErrorState && state.formValidation.routineNameValidationErrorMessage != null
//        )
//    }
    var endTime by remember { mutableStateOf(state.form.endTime) }
//    var isRoutineEndTimeError by remember {
//        mutableStateOf(
//            state is ValidationErrorState && state.formValidation.routineNameValidationErrorMessage != null
//        )
//    }
    var description by remember { mutableStateOf(state.form.description) }
//    var isRoutineDescriptionError by remember {
//        mutableStateOf(
//            state is ValidationErrorState && state.formValidation.routineNameValidationErrorMessage != null
//        )
//    }
    var remindersEnabled by remember { mutableStateOf(state.form.remindersEnabled) }
    if (showStartTimePicker) {
//        TimePickerDialog(onTimeSelected = {
//            startTime = it
//            process(TimePickedInput(startTime, true))
//        }) { process(TimePickedInput("", true)) }
    }
    if (showEndTimePicker) {
//        TimePickerDialog(onTimeSelected = {
//            endTime = it
//            process(TimePickedInput(endTime, false))
//        }) { process(TimePickedInput("", false)) }
    }

    when (state) {
        InitialState -> if (routineId != null) process(LoadRoutineByIdInput(routineId))
        is ReadyToSubmitState -> TODO()
        is ValidationErrorState -> TODO()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) { // fixme title center alignment
                IconButton(onClick = { process(CloseRoutineFormInput) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.close)
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.new_routine),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            // Routine Name
            TextField(
                value = routineName,
                onValueChange = {
                    routineName = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                routineName,
                                routineType,
                                routineCategory,
                                startTime,
                                endTime,
                                description,
                                remindersEnabled
                            )
                        )
                    )
                },
                label = { Text(text = stringResource(Res.string.routine_name)) },
                placeholder = { Text(text = stringResource(Res.string.morning_run)) },
                modifier = Modifier.fillMaxWidth(),
                isError = isRoutineNameError,
                supportingText = {
                    if (isRoutineNameError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = routineNameErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Routine Type
            TextField(
                value = routineType,
                onValueChange = {
                    routineType = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                routineName,
                                routineType,
                                routineCategory,
                                startTime,
                                endTime,
                                description,
                                remindersEnabled
                            )
                        )
                    )
                },
                label = { Text(text = stringResource(Res.string.routine_type)) },
                modifier = Modifier.fillMaxWidth(),
                isError = isRoutineTypeError,
                supportingText = {
                    if (isRoutineTypeError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = routineTypeErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Routine Category TODO add search dropdown
            TextField(
                value = routineCategory,
                onValueChange = {
                    routineCategory = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                routineName,
                                routineType,
                                routineCategory,
                                startTime,
                                endTime,
                                description,
                                remindersEnabled
                            )
                        )
                    )
                },
                label = { Text(text = stringResource(Res.string.routine_category)) },
                modifier = Modifier.fillMaxWidth(),
                isError = isRoutineTypeError,
                supportingText = {
                    if (isRoutineTypeError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = routineTypeErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Start Time and End Time
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = startTime,
                    onValueChange = {
                        startTime = it
                        process(
                            ValidateFormInput(
                                RoutineForm(
                                    routineName,
                                    routineType,
                                    routineCategory,
                                    startTime,
                                    endTime,
                                    description,
                                    remindersEnabled
                                )
                            )
                        )
                    },
                    label = { Text(text = stringResource(Res.string.start_time)) },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { process(ShowTimePickerInput(true)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = endTime, onValueChange = {
                        endTime = it
                        process(
                            ValidateFormInput(
                                RoutineForm(
                                    routineName,
                                    routineType,
                                    routineCategory,
                                    startTime,
                                    endTime,
                                    description,
                                    remindersEnabled
                                )
                            )
                        )
                    }, label = { Text(text = stringResource(Res.string.end_time)) },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { process(ShowTimePickerInput(false)) }
                )
            }
            // Description
            TextField(
                value = description,
                onValueChange = {
                    description = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                routineName,
                                routineType,
                                routineCategory,
                                startTime,
                                endTime,
                                description,
                                remindersEnabled
                            )
                        )
                    )
                },
                label = { Text(text = stringResource(Res.string.description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                shape = RoundedCornerShape(8.dp)
            )
            // Reminders Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(Res.string.reminders))
                Switch(checked = remindersEnabled, onCheckedChange = { remindersEnabled = it })
            }
        }
        // Create Routine Button
        Button(
            onClick = {
                process(
                    SubmitRoutineInput(
                        RoutineForm(
                            routineName,
                            routineType,
                            routineCategory,
                            startTime,
                            endTime,
                            description,
                            remindersEnabled
                        )
                    )
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color.Blue, contentColor = Color.White
            ), shape = RoundedCornerShape(8.dp)
        ) { Text(text = stringResource(Res.string.create_routine), fontSize = 16.sp) }
    }
}

@Preview
@Composable
fun PreviewNewRoutineScreen() {
//    NewRoutineScreenContent()
}