package com.applabs.eventx.details.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.applabs.eventx.events.domain.model.Event
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navHostController: NavHostController
) {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    var editMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!editMode) {
                        IconButton(onClick = { editMode = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            detailsState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            detailsState.event != null -> {
                EventDetailContent(
                    event = detailsState.event!!,
                    editMode = editMode,
                    onUpdateEvent = { updatedEvent ->
                        //detailsViewModel.updateEvent(updatedEvent, detailsState.event!!.id)
                        editMode = false
                    },
                    onCancelEdit = { editMode = false },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun EventDetailContent(
    event: Event,
    editMode: Boolean,
    onUpdateEvent: (Event) -> Unit,
    onCancelEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var eventName by remember { mutableStateOf(event.event_name) }
    var eventDescription by remember { mutableStateOf(event.event_description) }
    var eventLocation by remember { mutableStateOf(event.event_location) }
    var eventDate by remember { mutableStateOf(event.event_timeStamp) }
    var eventCategory by remember { mutableStateOf(event.category) }
    var eventDuration by remember { mutableStateOf(event.event_duration) }
    val eventParticipants =
        remember { mutableStateListOf(*event.event_participants.toTypedArray()) }
    var newParticipant by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Event Image
        Image(
            painter = rememberImagePainter(data = "https://via.placeholder.com/400"),
            contentDescription = "Event Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Event Name
        if (editMode) {
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = eventName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Description Card
        if (editMode) {
            OutlinedTextField(
                value = eventDescription,
                onValueChange = { eventDescription = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            EventDetailCard(title = "Description", content = eventDescription)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Location and Date/Time
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
        Spacer(modifier = Modifier.height(8.dp))

        // Category and Duration
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (editMode) {
                OutlinedTextField(
                    value = eventCategory,
                    onValueChange = { eventCategory = it },
                    label = { Text("Category") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = eventDuration,
                    onValueChange = { eventDuration = it },
                    label = { Text("Duration") },
                    modifier = Modifier.weight(1f)
                )
            } else {
                EventDetailIconCard(
                    icon = Icons.Default.Category,
                    text = eventCategory,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                EventDetailIconCard(
                    icon = Icons.Default.Schedule,
                    text = eventDuration,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Participants
        if (editMode) {
            OutlinedTextField(
                value = newParticipant,
                onValueChange = { newParticipant = it },
                label = { Text("Add Participant") },
                trailingIcon = {
                    IconButton(onClick = {
                        if (newParticipant.isNotEmpty()) {
                            eventParticipants.add(newParticipant)
                            newParticipant = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Participant")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(eventParticipants.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = eventParticipants[index],
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            eventParticipants.removeAt(index)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Participant",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        } else {
            EventDetailCard(
                title = "Participants",
                content = eventParticipants.joinToString(", ")
            )
        }

        if (editMode) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onCancelEdit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val updatedEvent = event.copy(
                            event_name = eventName,
                            event_description = eventDescription,
                            event_location = eventLocation,
                            event_timeStamp = eventDate,
                            event_duration = eventDuration,
                            event_participants = eventParticipants.toList(),
                            category = eventCategory
                        )
                        onUpdateEvent(updatedEvent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Update")
                }
            }
        }
    }
}

@Composable
fun EventDetailCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun EventDetailIconCard(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
