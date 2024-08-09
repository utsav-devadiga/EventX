package com.applabs.eventx.events.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.applabs.eventx.events.data.remote.response.EventDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEventScreenForm(
    navHostController: NavHostController,
    eventListViewModel: EventListViewModel = hiltViewModel()
) {
    val addEventSuccess by eventListViewModel.addEventSuccess.collectAsState(initial = false)

    LaunchedEffect(addEventSuccess) {
        if (addEventSuccess) {
            eventListViewModel.resetAddEventSuccess()
            navHostController.popBackStack()
        }
    }

    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDuration by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventCategory by remember { mutableStateOf("") }
    var newParticipant by remember { mutableStateOf("") }

    val eventParticipants = remember { mutableStateListOf<String>() }

    var errorState by remember {
        mutableStateOf(
            mapOf(
                "eventName" to false,
                "eventDate" to false,
                "eventTime" to false,
                "eventLocation" to false,
                "eventDuration" to false,
                "eventDescription" to false,
                "eventCategory" to false,
                "newParticipant" to false
            )
        )
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Event") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val isValid = validateFields(
                            listOf(
                                Triple("Event Name", eventName) { value -> eventName = value },
                                Triple("Event Date", eventDate) { value -> eventDate = value },
                                Triple("Event Time", eventTime) { value -> eventTime = value },
                                Triple("Event Location", eventLocation) { value ->
                                    eventLocation = value
                                },
                                Triple("Event Duration", eventDuration) { value ->
                                    eventDuration = value
                                },
                                Triple(
                                    "Event Description",
                                    eventDescription
                                ) { value -> eventDescription = value },
                                Triple("Event Category", eventCategory) { value ->
                                    eventCategory = value
                                }
                            ),
                            setError = { label, hasError ->
                                errorState =
                                    errorState.toMutableMap().apply { this[label] = hasError }
                            }
                        )
                        if (isValid) {
                            val eventTimeStamp = "$eventDate $eventTime"
                            val eventDto = EventDto(
                                event_name = eventName,
                                event_timeStamp = eventTimeStamp,
                                event_location = eventLocation,
                                event_duration = eventDuration,
                                event_participants = eventParticipants,
                                event_description = eventDescription,
                                category = eventCategory
                            )
                            eventListViewModel.addEvents(eventDto)
                        }
                    }) {
                        Icon(Icons.Default.Done, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Make the whole content scrollable with a verticalScroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr) + 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(data = "https://via.placeholder.com/150"),
                contentDescription = "Event Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = eventName,
                onValueChange = { value ->
                    eventName = value
                    errorState =
                        errorState.toMutableMap().apply { this["eventName"] = value.isEmpty() }
                },
                label = { Text("Event Title") },
                isError = errorState["eventName"] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState["eventName"] == true) {
                Text(
                    "Event Name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 0.8.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .clickable {
                            val datePickerDialog = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val formattedDate =
                                        String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                                    eventDate = formattedDate
                                    errorState = errorState
                                        .toMutableMap()
                                        .apply { this["eventDate"] = false }
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePickerDialog.show()
                        }
                ) {
                    Text(
                        text = eventDate.ifBlank { "Date" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 0.8.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .clickable {
                            val timePickerDialog = TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    val formattedTime =
                                        String.format("%02d:%02d", hourOfDay, minute)
                                    eventTime = formattedTime
                                    errorState = errorState
                                        .toMutableMap()
                                        .apply { this["eventTime"] = false }
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            )
                            timePickerDialog.show()
                        }
                ) {
                    Text(
                        text = eventTime.ifBlank { "Time" },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            if (errorState["eventDate"] == true) {
                Text(
                    "Event Date cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (errorState["eventTime"] == true) {
                Text(
                    "Event Time cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = eventLocation,
                onValueChange = { value ->
                    eventLocation = value
                    errorState =
                        errorState.toMutableMap().apply { this["eventLocation"] = value.isEmpty() }
                },
                label = { Text("Location") },
                isError = errorState["eventLocation"] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState["eventLocation"] == true) {
                Text(
                    "Event Location cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = eventDuration,
                onValueChange = { value ->
                    eventDuration = value
                    errorState =
                        errorState.toMutableMap().apply { this["eventDuration"] = value.isEmpty() }
                },
                label = { Text("Duration") },
                isError = errorState["eventDuration"] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState["eventDuration"] == true) {
                Text(
                    "Event Duration cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = eventCategory,
                onValueChange = { value ->
                    eventCategory = value
                    errorState =
                        errorState.toMutableMap().apply { this["eventCategory"] = value.isEmpty() }
                },
                label = { Text("Category") },
                isError = errorState["eventCategory"] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState["eventCategory"] == true) {
                Text(
                    "Event Category cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = eventDescription,
                onValueChange = { value ->
                    eventDescription = value
                    errorState = errorState.toMutableMap()
                        .apply { this["eventDescription"] = value.isEmpty() }
                },
                label = { Text("Description") },
                isError = errorState["eventDescription"] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState["eventDescription"] == true) {
                Text(
                    "Event Description cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newParticipant,
                onValueChange = { value ->
                    newParticipant = value
                    errorState =
                        errorState.toMutableMap().apply { this["newParticipant"] = value.isEmpty() }
                },
                label = { Text("Add Participant") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (newParticipant.isNotEmpty()) {
                                eventParticipants.add(newParticipant)
                                newParticipant = ""
                            } else {
                                errorState = errorState.toMutableMap()
                                    .apply { this["newParticipant"] = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Participant"
                        )
                    }
                },
                isError = errorState["newParticipant"] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState["newParticipant"] == true) {
                Text(
                    "Participant cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Horizontally scrollable participants list with delete functionality
            LazyRow {
                items(eventParticipants.size) { index ->
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                            .border(
                                width = 1.dp, color = MaterialTheme.colorScheme.onBackground,
                                CircleShape
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Participant",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .padding(2.dp)

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = eventParticipants[index],
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                eventParticipants.removeAt(index)  // Remove the participant from the list
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Participant",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val isValid = validateFields(
                        listOf(
                            Triple("Event Name", eventName) { value -> eventName = value },
                            Triple("Event Date", eventDate) { value -> eventDate = value },
                            Triple("Event Time", eventTime) { value -> eventTime = value },
                            Triple("Event Location", eventLocation) { value ->
                                eventLocation = value
                            },
                            Triple("Event Duration", eventDuration) { value ->
                                eventDuration = value
                            },
                            Triple(
                                "Event Description",
                                eventDescription
                            ) { value -> eventDescription = value }
                        ),
                        setError = { label, hasError ->
                            errorState = errorState.toMutableMap().apply { this[label] = hasError }
                        }
                    )
                    if (isValid) {
                        val eventTimeStamp =
                            convertToTimestamp(eventDate, eventTime)?.takeIf { it.isNotBlank() }
                                ?: ""
                        val eventDto = EventDto(
                            event_name = eventName,
                            event_timeStamp = eventTimeStamp,
                            event_location = eventLocation,
                            event_duration = eventDuration,
                            event_participants = eventParticipants,
                            event_description = eventDescription,
                            category = eventCategory
                        )
                        eventListViewModel.addEvents(eventDto)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Event")
            }
        }
    }
}

fun validateFields(
    fields: List<Triple<String, String, (String) -> Unit>>,
    setError: (String, Boolean) -> Unit
): Boolean {
    var isValid = true
    fields.forEach { (label, value, _) ->
        if (value.isEmpty()) {
            setError(label, true)
            isValid = false
        } else {
            setError(label, false)
        }
    }
    return isValid
}

fun convertToTimestamp(eventDate: String, eventTime: String): String? {
    val dateTimeString = "$eventDate $eventTime"
    val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = inputFormat.parse(dateTimeString)
    return date?.let { outputFormat.format(it) }
}