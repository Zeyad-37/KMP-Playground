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
import com.zeyadgasser.playground.routine.form.viewmodel.CloseCreateRoutineEffect
import com.zeyadgasser.playground.routine.form.viewmodel.CloseRoutineFormInput
import com.zeyadgasser.playground.routine.form.viewmodel.HideTimePickerEffect
import com.zeyadgasser.playground.routine.form.viewmodel.LoadRoutineByIdInput
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineForm
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.InitialState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ReadyToSubmitState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormState.ValidationErrorState
import com.zeyadgasser.playground.routine.form.viewmodel.RoutineFormValidation
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
    if (showStartTimePicker) {
        WheelTimePickerBottomSheet(onTimeSelected = { process(TimePickedInput(it, true)) })
        { process(TimePickedInput("", true)) }
    }
    if (showEndTimePicker) {
        WheelTimePickerBottomSheet(onTimeSelected = { process(TimePickedInput(it, false)) })
        { process(TimePickedInput("", false)) }
    }
    when (state) {
        InitialState -> if (routineId != null) process(LoadRoutineByIdInput(routineId))
        is ReadyToSubmitState, is ValidationErrorState -> Unit
    }
    val form: RoutineForm = state.form
    val validationErrors: RoutineFormValidation? = (state as? ValidationErrorState)?.formValidation
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
                        contentDescription = "Close",
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isCreate) "Track New Routine" else "Edit Routine",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            // Routine Name
            TextField(
                value = form.name,
                onValueChange = { process(ValidateFormInput(form.copy(name = it))) },
//                label = { Text(text = stringResource(Res.string.routine_name)) },
                label = { Text(text = "Routine Name") },
//                placeholder = { Text(text = stringResource(Res.string.morning_run)) },
                placeholder = { Text(text = "Morning Run") },
                modifier = Modifier.fillMaxWidth(),
                isError = validationErrors?.nameValidationErrorMessage != null,
                supportingText = {
                    if (validationErrors?.nameValidationErrorMessage != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = validationErrors.nameValidationErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Routine Type
            TextField(
                value = form.type,
                onValueChange = { process(ValidateFormInput(form.copy(type = it))) },
//                label = { Text(text = stringResource(Res.string.routine_type)) },
                label = { Text(text = "Routine Type") },
                modifier = Modifier.fillMaxWidth(),
                isError = validationErrors?.typeValidationErrorMessage != null,
                supportingText = {
                    if (validationErrors?.typeValidationErrorMessage != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = validationErrors.typeValidationErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Routine Category TODO add search dropdown
            TextField(
                value = form.category,
                onValueChange = { process(ValidateFormInput(form.copy(category = it))) },
//                label = { Text(text = stringResource(Res.string.routine_category)) },
                label = { Text(text = "Routine Category") },
                modifier = Modifier.fillMaxWidth(),
                isError = validationErrors?.categoryValidationErrorMessage != null,
                supportingText = {
                    if (validationErrors?.categoryValidationErrorMessage != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = validationErrors.categoryValidationErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
            // Start Time and End Time
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = form.startTime,
                    onValueChange = { process(ValidateFormInput(form.copy(startTime = it))) },
                    isError = validationErrors?.startTimeValidationErrorMessage != null,
//                    label = { Text(text = stringResource(Res.string.start_time)) },
                    label = { Text(text = "Start Time") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { process(ShowTimePickerInput(true)) },
                    supportingText = {
                        if (validationErrors?.startTimeValidationErrorMessage != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = validationErrors.startTimeValidationErrorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = form.endTime,
                    onValueChange = { process(ValidateFormInput(form.copy(endTime = it))) },
//                    label = { Text(text = stringResource(Res.string.end_time)) },
                    label = { Text(text = "End Time") },
                    isError = validationErrors?.endTimeValidationErrorMessage != null,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { process(ShowTimePickerInput(false)) },
                    supportingText = {
                        if (validationErrors?.endTimeValidationErrorMessage != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = validationErrors.endTimeValidationErrorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
            }
            // Description
            TextField(
                value = form.description,
                onValueChange = { process(ValidateFormInput(form.copy(description = it))) },
                isError = validationErrors?.descriptionValidationErrorMessage != null,
//                label = { Text(text = stringResource(Res.string.description)) },
                label = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                shape = RoundedCornerShape(8.dp),
                supportingText = {
                    if (validationErrors?.descriptionValidationErrorMessage != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = validationErrors.descriptionValidationErrorMessage,
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
                Switch(
                    checked = form.remindersEnabled,
                    onCheckedChange = { process(ValidateFormInput(form.copy(remindersEnabled = it))) }
                )
            }
        }
        // Create Routine Button
        Button(
            enabled = state is ReadyToSubmitState,
            onClick = { process(SubmitRoutineInput(form, routineId)) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(),
            shape = RoundedCornerShape(8.dp)
        ) {
//                text = stringResource(if (isCreate) Res.string.create_routine else Res.string.update_routine),
            Text(text = if (isCreate) "Create Routine" else "Update Routine", fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
fun PreviewNewRoutineScreen() {
//    NewRoutineScreenContent()
}
