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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.zeyadgasser.playground.routine.form.resources.Res
import com.zeyadgasser.playground.routine.form.resources.close
import com.zeyadgasser.playground.routine.form.resources.create_routine
import com.zeyadgasser.playground.routine.form.resources.description
import com.zeyadgasser.playground.routine.form.resources.end_time
import com.zeyadgasser.playground.routine.form.resources.ic_close
import com.zeyadgasser.playground.routine.form.resources.morning_run
import com.zeyadgasser.playground.routine.form.resources.new_routine
import com.zeyadgasser.playground.routine.form.resources.reminders
import com.zeyadgasser.playground.routine.form.resources.routine_category
import com.zeyadgasser.playground.routine.form.resources.routine_name
import com.zeyadgasser.playground.routine.form.resources.routine_type
import com.zeyadgasser.playground.routine.form.resources.start_time
import com.zeyadgasser.playground.routine.form.resources.update_routine
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
import com.zeyadgasser.playground.routine.form.viewmodel.TimePickedInput
import com.zeyadgasser.playground.routine.form.viewmodel.ValidateFormInput
import kotlinx.coroutines.flow.collectLatest
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
        routinesState, showStartDialog, showEndDialog, routineId
    ) { viewModel.process(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFormScreenContent(
    state: RoutineFormState,
    showStartTimePicker: Boolean,
    showEndTimePicker: Boolean,
    routineId: Long? = null,
    process: (Input) -> Unit,
) {
    var isCreate by remember { mutableStateOf(routineId == null) }
    var name by remember { mutableStateOf(state.form.routineName) }
    var isNameError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.nameValidationErrorMessage != null)
    }
    var nameErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.nameValidationErrorMessage.orEmpty())
    }
    var type by remember { mutableStateOf(state.form.routineType) }
    var isTypeError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.typeValidationErrorMessage != null)
    }
    var typeErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.typeValidationErrorMessage.orEmpty())
    }
    var category by remember { mutableStateOf(state.form.routineCategory) }
    var isCategoryError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.categoryValidationErrorMessage != null)
    }
    var categoryErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.categoryValidationErrorMessage.orEmpty())
    }
    var startTime by remember { mutableStateOf(state.form.startTime) }
    var isStartTimeError by remember {
        mutableStateOf(
            state is ValidationErrorState && state.formValidation.startTimeValidationErrorMessage != null
        )
    }
    var startTimeErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.startTimeValidationErrorMessage.orEmpty())
    }
    var endTime by remember { mutableStateOf(state.form.endTime) }
    var isEndTimeError by remember {
        mutableStateOf(
            state is ValidationErrorState && state.formValidation.endTimeValidationErrorMessage != null
        )
    }
    var endTimeErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.endTimeValidationErrorMessage.orEmpty())
    }
    var description by remember { mutableStateOf(state.form.description) }
    var isDescriptionError by remember {
        mutableStateOf(
            state is ValidationErrorState && state.formValidation.descriptionValidationErrorMessage != null
        )
    }
    var descriptionTimeErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.descriptionValidationErrorMessage.orEmpty())
    }
    var remindersEnabled by remember { mutableStateOf(state.form.remindersEnabled) }
    if (showStartTimePicker) {
        WheelTimePickerBottomSheet(onTimeSelected = {
            startTime = it
            process(TimePickedInput(startTime, true))
        }) {
            process(TimePickedInput("", true))
        }
    }
    if (showEndTimePicker) {
        WheelTimePickerBottomSheet(onTimeSelected = {
            endTime = it
            process(TimePickedInput(endTime, false))
        }) {
            process(TimePickedInput("", false))
        }
    }

    when (state) {
        InitialState -> if (routineId != null) process(LoadRoutineByIdInput(routineId))
        is ReadyToSubmitState -> with(state) {
            name = form.routineName
            type = form.routineType
            category = form.routineCategory
            startTime = form.startTime
            endTime = form.endTime
            description = form.description
            remindersEnabled = form.remindersEnabled
        }

        is ValidationErrorState -> with(state.formValidation) {
            nameErrorMessage = nameValidationErrorMessage.orEmpty()
            typeErrorMessage = typeValidationErrorMessage.orEmpty()
            categoryErrorMessage = categoryValidationErrorMessage.orEmpty()
        }
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
                        imageVector = Icons.Default.Close,
//                        painter = painterResource(Res.drawable.ic_close),
//                        contentDescription = stringResource(Res.string.close),
                        contentDescription = "Close",
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
//                        text = stringResource(Res.string.new_routine),
                        text = "New Routine",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            // Routine Name
            TextField(
                value = name,
                onValueChange = {
                    name = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                name, type, category, startTime, endTime, description, remindersEnabled
                            )
                        )
                    )
                },
//                label = { Text(text = stringResource(Res.string.routine_name)) },
                label = { Text(text = "Routine Name") },
//                placeholder = { Text(text = stringResource(Res.string.morning_run)) },
                placeholder = { Text(text = "Morning Run") },
                modifier = Modifier.fillMaxWidth(),
                isError = isNameError,
                supportingText = {
                    if (isNameError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = nameErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Routine Type
            TextField(
                value = type,
                onValueChange = {
                    type = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                name, type, category, startTime, endTime, description, remindersEnabled
                            )
                        )
                    )
                },
//                label = { Text(text = stringResource(Res.string.routine_type)) },
                label = { Text(text = "Routine Type") },
                modifier = Modifier.fillMaxWidth(),
                isError = isTypeError,
                supportingText = {
                    if (isTypeError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = typeErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Routine Category TODO add search dropdown
            TextField(
                value = category,
                onValueChange = {
                    category = it
                    process(
                        ValidateFormInput(
                            RoutineForm(
                                name, type, category, startTime, endTime, description, remindersEnabled
                            )
                        )
                    )
                },
//                label = { Text(text = stringResource(Res.string.routine_category)) },
                label = { Text(text = "Routine Category") },
                modifier = Modifier.fillMaxWidth(),
                isError = isCategoryError,
                supportingText = {
                    if (isTypeError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = categoryErrorMessage,
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
                                    name, type, category, startTime, endTime, description, remindersEnabled
                                )
                            )
                        )
                    },
                    isError = isStartTimeError,
//                    label = { Text(text = stringResource(Res.string.start_time)) },
                    label = { Text(text = "Start Time") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { process(ShowTimePickerInput(true)) },
                    supportingText = {
                        if (isStartTimeError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = startTimeErrorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = endTime,
                    onValueChange = {
                        endTime = it
                        process(
                            ValidateFormInput(
                                RoutineForm(
                                    name, type, category, startTime, endTime, description, remindersEnabled
                                )
                            )
                        )
                    },
//                    label = { Text(text = stringResource(Res.string.end_time)) },
                    label = { Text(text = "End Time") },
                    isError = isEndTimeError,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { process(ShowTimePickerInput(false)) },
                    supportingText = {
                        if (isEndTimeError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = endTimeErrorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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
                                name, type, category, startTime, endTime, description, remindersEnabled
                            )
                        )
                    )
                },
                isError = isDescriptionError,
//                label = { Text(text = stringResource(Res.string.description)) },
                label = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                shape = RoundedCornerShape(8.dp),
                supportingText = {
                    if (isDescriptionError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = descriptionTimeErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Reminders Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Text(text = stringResource(Res.string.reminders))
                Text(text = "Reminders")
                Switch(checked = remindersEnabled, onCheckedChange = { remindersEnabled = it })
            }
        }
        // Create Routine Button
        Button(
            enabled = state is ReadyToSubmitState,
            onClick = {
                process(
                    SubmitRoutineInput(
                        RoutineForm(
                            name, type, category, startTime, endTime, description, remindersEnabled
                        ), routineId
                    )
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), colors = ButtonDefaults.buttonColors(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
//                text = stringResource(if (isCreate) Res.string.create_routine else Res.string.update_routine),
                text = if (isCreate) "Create Routine" else "Update Routine",
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewNewRoutineScreen() {
//    NewRoutineScreenContent()
}
