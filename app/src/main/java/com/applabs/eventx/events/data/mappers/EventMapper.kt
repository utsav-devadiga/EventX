package com.applabs.eventx.events.data.mappers

import com.applabs.eventx.events.data.local.event.EventEntity
import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.domain.model.Event

/**
 * @author Utsav Devadiga
 */
fun EventDto.toEventEntity(

): EventEntity {
    return EventEntity(
        event_name = event_name,
        event_timeStamp = event_timeStamp,
        event_duration = event_duration,
        event_description = event_description,
        event_location = event_location,
        category = category,
        event_participants = try {
            event_participants?.joinToString(",") ?: "No Participants"
        } catch (e: Exception) {
            "No Participants"
        }
    )
}

fun EventEntity.toEvent(

): Event {
    return Event(
        event_name = event_name,
        event_timeStamp = event_timeStamp,
        event_duration = event_duration,
        event_description = event_description,
        event_location = event_location,
        event_id = event_id,
        category = category,
        event_participants = try {
            event_participants.split(",").map { it.toString() }
        } catch (e: Exception) {
            emptyList()
        }
    )
}