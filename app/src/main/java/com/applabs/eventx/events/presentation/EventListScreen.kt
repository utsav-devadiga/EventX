package com.applabs.eventx.events.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.applabs.eventx.events.presentation.components.EventItem

/**
 * @author Utsav Devadiga
 */
@Composable
fun EventListScreen(
    navHostController: NavHostController,
    onEvent: (EventListUiEvent) -> Unit,
    eventListViewModel: EventListViewModel
) {

    val eventListState by eventListViewModel.eventListState.collectAsState()


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


    }

}