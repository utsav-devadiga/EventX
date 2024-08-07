package com.applabs.eventx.events.presentation

import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.domain.model.Event

/**
 * @author Utsav Devadiga
 */
data class EventListState(

    val isLoading: Boolean = false,

    val isCurrentPageDashboard: Boolean = true,

    val eventList: List<Event> = emptyList(),



)