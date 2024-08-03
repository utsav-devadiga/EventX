package com.applabs.eventx.events.data.remote.response

/**
 * @author Utsav Devadiga
 */
data class EventDto(
    val event_name: String,
    val event_timeStamp: String,
    val event_location: String,
    val event_duration: String,
    val event_description: String,
    val event_participants: List<String>?,
    val category: String,
    val event_id: Int
)