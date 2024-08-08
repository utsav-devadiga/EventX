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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.presentation.components.EventItem
import com.applabs.eventx.events.util.Screen

/**
 * @author Utsav Devadiga
 */
@Composable
fun DashboardEventScreen(
    navHostController: NavHostController,
    onEvent: (EventListUiEvent) -> Unit,
    eventListViewModel: EventListViewModel
) {

    val eventListState by eventListViewModel.eventListState.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        if (eventListState.eventList.isEmpty()) {
            //empty state
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Screen.NewEvent.route)
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
                    navHostController.navigate(Screen.NewEvent.route)
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

