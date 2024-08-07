package com.applabs.eventx.events.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.presentation.components.EventItem
import kotlinx.coroutines.launch

/**
 * @author Utsav Devadiga
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardEventScreen(
    navHostController: NavHostController,
    onEvent: (EventListUiEvent) -> Unit
) {
    val eventListViewModel = hiltViewModel<EventListViewModel>()
    val eventListState by eventListViewModel.eventListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // State for the bottom sheet
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    // Observe add event success
    val addEventSuccess by eventListViewModel.addEventSuccess.collectAsState(initial = false)

    // Close the bottom sheet when event is successfully added
    LaunchedEffect(addEventSuccess) {
        if (addEventSuccess) {
            bottomSheetState.hide()
            eventListViewModel.resetAddEventSuccess()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            BottomSheetContent(
                onAddEvent = { eventDto ->
                    eventListViewModel.addEvents(eventDto)
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (eventListState.eventList.isEmpty()) {
                //empty state
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch { bottomSheetState.show() }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(eventListState.eventList.size) { index ->
                        EventItem(eventListState.eventList[index], navHostController)
                    }
                }

                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch { bottomSheetState.show() }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(onAddEvent: (EventDto) -> Unit) {
    var eventName by remember { mutableStateOf("") }
    var eventTimeStamp by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDuration by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventCategory by remember { mutableStateOf("") }
    var newParticipant by remember { mutableStateOf("") }

    val eventParticipants = remember { mutableStateListOf<String>() }

    var errorState by remember { mutableStateOf(mapOf(
        "eventName" to false,
        "eventTimeStamp" to false,
        "eventLocation" to false,
        "eventDuration" to false,
        "eventDescription" to false,
        "eventCategory" to false,
        "newParticipant" to false
    )) }

    val fields = listOf(
        Triple("Event Name", eventName, { value: String -> eventName = value }),
        Triple("Event Timestamp", eventTimeStamp, { value: String -> eventTimeStamp = value }),
        Triple("Event Location", eventLocation, { value: String -> eventLocation = value }),
        Triple("Event Duration", eventDuration, { value: String -> eventDuration = value }),
        Triple("Event Description", eventDescription, { value: String -> eventDescription = value }),
        Triple("Event Category", eventCategory, { value: String -> eventCategory = value })
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Add New Event", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = {
                    val isValid = validateFields(
                        fields,
                        setError = { label, hasError -> errorState = errorState.toMutableMap().apply { this[label] = hasError } }
                    )
                    if (isValid) {
                        val eventDto = EventDto(
                            event_name = eventName,
                            event_timeStamp = eventTimeStamp,
                            event_location = eventLocation,
                            event_duration = eventDuration,
                            event_participants = eventParticipants,
                            event_description = eventDescription,
                            category = eventCategory
                        )
                        onAddEvent(eventDto)
                    }
                }
            ) {
                Text("Add Event")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))



        fields.forEach { (label, value, onValueChange) ->
            OutlinedTextField(
                maxLines = 1,
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                    errorState = errorState.toMutableMap().apply { this[label] = newValue.isEmpty() }
                },
                label = { Text(label) },
                isError = errorState[label] == true,
                modifier = Modifier.fillMaxWidth()
            )
            if (errorState[label] == true) {
                Text("$label cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // New Participant Input with Trailing Icon
        OutlinedTextField(
            maxLines = 1,
            value = newParticipant,
            onValueChange = {
                newParticipant = it
                errorState = errorState.toMutableMap().apply { this["newParticipant"] = it.isEmpty() }
            },
            label = { Text("Add Participant") },
            isError = errorState["newParticipant"] == true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (newParticipant.isNotEmpty()) {
                            eventParticipants.add(newParticipant)
                            newParticipant = ""
                        } else {
                            errorState = errorState.toMutableMap().apply { this["newParticipant"] = true }
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Participant")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorState["newParticipant"] == true) {
            Text("Participant cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Display Participants
        LazyColumn {
            items(eventParticipants.size) { index ->
                Text(text = eventParticipants[index])
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

