package com.applabs.eventx.events.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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
                EventItem(eventListState.eventList[index],navHostController)
            }
        }


    }
}