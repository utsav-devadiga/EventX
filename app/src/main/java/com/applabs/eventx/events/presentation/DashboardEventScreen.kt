package com.applabs.eventx.events.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.presentation.components.EventItem

/**
 * @author Utsav Devadiga
 */
@Composable
fun DashboardEventScreen(
    eventListState: EventListState,
    navHostController: NavHostController,
    onEvent: (EventListUiEvent) -> Unit
) {

    val eventListViewModel = hiltViewModel<EventListViewModel>()

    if (eventListState.eventList.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else {
        LazyColumn {
            items(eventListState.eventList.size) { index ->
                EventItem(eventListState.eventList[index], navHostController)
            }
        }

        FloatingActionButton(
            onClick = {

                val eventDto = EventDto(
                    event_name = "event name",
                    event_timeStamp = "12345667",
                    event_location = "LOCATION",
                    event_duration = "123143114",
                    event_participants = mutableListOf("1", "2", "3"),
                    event_description = "test description",
                    category = "test category",
                    event_id = 5

                )

                eventListViewModel.addEvents(eventDto)
                Log.d("DATABASE OPERATION", "addEvent: from dashboard")
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
        }
    }
}