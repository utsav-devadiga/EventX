package com.applabs.eventx.details.presentation

import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.domain.model.Event

/**
 * @author Utsav Devadiga
 */
data class DetailsState(
    val isLoading: Boolean = false,
    val event: Event? = null
)