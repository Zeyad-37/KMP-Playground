package com.zeyadgasser.playground.routine.form.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.chaintech.kmp_date_time_picker.ui.timepicker.WheelTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.timeToString

@Composable
fun WheelTimePickerBottomSheet(onTimeSelected: (String) -> Unit, onDismiss: () -> Unit) { // fixme scroll!
    var selectedTime by remember { mutableStateOf("") }
    WheelTimePickerView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp, bottom = 26.dp),
        showTimePicker = true,
        titleStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
        ),
        doneLabelStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF007AFF),
        ),
        timeFormat = TimeFormat.AM_PM,
        selectorProperties = WheelPickerDefaults.selectorProperties(
            borderColor = Color.LightGray,
        ),
        rowCount = 5,
        height = 170.dp,
        selectedTextStyle = TextStyle(
            fontWeight = FontWeight(600),
        ),
        dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
        dragHandle = {
            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp).width(50.dp).clip(CircleShape),
                thickness = 4.dp,
                color = Color(0xFFE8E4E4)
            )
        },
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        onDoneClick = {
            selectedTime = timeToString(it, "hh:mm a")
            onTimeSelected(selectedTime)
        },
        onDismiss = { onDismiss() }
    )
}