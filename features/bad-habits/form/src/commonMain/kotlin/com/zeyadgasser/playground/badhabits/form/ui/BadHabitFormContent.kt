package com.zeyadgasser.playground.badhabits.form.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeyadgasser.playground.architecture.presentation.Input
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitForm
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.InitialState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ReadyToSubmitState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ValidationErrorState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormValidation
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormViewModel
import com.zeyadgasser.playground.badhabits.form.viewmodel.CloseBadHabitFormInput
import com.zeyadgasser.playground.badhabits.form.viewmodel.CloseCreateBadHabitEffect
import com.zeyadgasser.playground.badhabits.form.viewmodel.LoadBadHabitByIdInput
import com.zeyadgasser.playground.badhabits.form.viewmodel.SubmitBadHabitInput
import com.zeyadgasser.playground.badhabits.form.viewmodel.ValidateFormInput
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun BadHabitFormStateHolder(
    viewModel: BadHabitFormViewModel = koinInject(), badHabitId: Long? = null, onCloseFormClick: () -> Unit,
) {
    val badHabitsState by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                CloseCreateBadHabitEffect -> onCloseFormClick()
            }
        }
    }
    BadHabitFormContent(badHabitsState, badHabitId) { viewModel.process(it) }
}

@Composable
fun BadHabitFormContent(state: BadHabitFormState, badHabitId: Long? = null, process: (Input) -> Unit) {
    var isCreate by remember { mutableStateOf(badHabitId == null) }
    when (state) {
        InitialState -> if (badHabitId != null) process(LoadBadHabitByIdInput(badHabitId))
        is ValidationErrorState, is ReadyToSubmitState -> Unit
    }
    val form: BadHabitForm = state.form
    val validationErrors: BadHabitFormValidation? = (state as? ValidationErrorState)?.formValidation
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 56.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { process(CloseBadHabitFormInput) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Gray
                )
            }
            Text(
                text = if (isCreate) "Track New Bad Habit" else "Edit Bad Habit",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        // Habit Name
        OutlinedTextField(
            value = form.name,
            onValueChange = { process(ValidateFormInput(form.copy(name = it))) },
            label = { Text(text = "Habit Name") },
            isError = validationErrors?.nameValidationErrorMessage != null,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            placeholder = { Text(text = "e.g., Smoking") },
        )
        if (validationErrors?.nameValidationErrorMessage != null) {
            Text(
                text = validationErrors.nameValidationErrorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        // Description
        OutlinedTextField(
            value = form.description,
            onValueChange = { process(ValidateFormInput(form.copy(description = it))) },
            label = { Text(text = "Description") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(100.dp),
            maxLines = 5,
            isError = validationErrors?.descriptionValidationErrorMessage != null,
        )
        if (validationErrors?.descriptionValidationErrorMessage != null) {
            Text(
                text = validationErrors.descriptionValidationErrorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        // Frequency
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Frequency",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            DropdownMenu(
                selectedOption = if (form.frequency.isBlank()) "Daily" else form.frequency,
                onOptionSelected = { process(ValidateFormInput(form.copy(frequency = it))) },
                options = listOf("Daily", "Weekly", "Monthly")
            )
        }
        if (validationErrors?.frequencyValidationErrorMessage != null) {
            Text(
                text = validationErrors.frequencyValidationErrorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        // Reminders
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reminders",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = form.reminders,
                onCheckedChange = { process(ValidateFormInput(form.copy(reminders = it))) }
            )
        }
        // Save Button
        Button(
            onClick = { process(SubmitBadHabitInput(form, badHabitId)) },
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) { Text(text = if (isCreate) "Create Bad Habit" else "Update Bad Habit", fontSize = 18.sp) }
    }
}
