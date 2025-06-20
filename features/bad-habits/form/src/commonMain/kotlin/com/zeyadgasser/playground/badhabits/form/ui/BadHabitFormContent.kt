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
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ReadyToSubmitState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState.ValidationErrorState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormViewModel
import com.zeyadgasser.playground.badhabits.form.viewmodel.CloseBadHabitFormInput
import com.zeyadgasser.playground.badhabits.form.viewmodel.CloseCreateBadHabitEffect
import com.zeyadgasser.playground.badhabits.form.viewmodel.LoadBadHabitByIdInput
import com.zeyadgasser.playground.badhabits.form.viewmodel.SubmitBadHabitInput
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
    var name by remember { mutableStateOf(state.form.name) }
    var isNameError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.nameValidationErrorMessage != null)
    }
    var nameErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.nameValidationErrorMessage.orEmpty())
    }
    var frequency by remember {
        mutableStateOf(if (state.form.frequency.isBlank()) "Daily" else state.form.frequency.toString())
    }
    var isFrequencyError by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.frequencyValidationErrorMessage != null)
    }
    var frequencyErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.frequencyValidationErrorMessage.orEmpty())
    }
    var description by remember { mutableStateOf(state.form.description) }
    var isDescriptionError by remember {
        mutableStateOf(
            state is ValidationErrorState && state.formValidation.descriptionValidationErrorMessage != null
        )
    }
    var descriptionErrorMessage by remember {
        mutableStateOf((state as? ValidationErrorState)?.formValidation?.descriptionValidationErrorMessage.orEmpty())
    }
    var reminders by remember { mutableStateOf(false) }

    when (state) {
        BadHabitFormState.InitialState -> if (badHabitId != null) process(LoadBadHabitByIdInput(badHabitId))
        is ReadyToSubmitState -> with(state) {
            name = form.name
            frequency = form.frequency
            description = form.description
            reminders = form.reminders
        }

        is ValidationErrorState -> with(state.formValidation) {
            nameErrorMessage = nameValidationErrorMessage.orEmpty()
            frequencyErrorMessage = frequencyValidationErrorMessage.orEmpty()
            descriptionErrorMessage = descriptionValidationErrorMessage.orEmpty()
        }
    }

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
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Habit Name") },
            placeholder = { Text(text = "e.g., Smoking") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            isError = isNameError,
        )
        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(100.dp),
            maxLines = 5,
            isError = isDescriptionError,
        )
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
                selectedOption = frequency,
                onOptionSelected = { frequency = it },
                options = listOf("Daily", "Weekly", "Monthly")
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
            Switch(checked = reminders, onCheckedChange = { reminders = it })
        }
        // Save Button
        Button(
            onClick = {
                process(
                    SubmitBadHabitInput(BadHabitForm(name, frequency, description, reminders), badHabitId)
                )
            },
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) { Text(text = "Save", fontSize = 18.sp) }
    }
}
