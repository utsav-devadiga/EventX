package com.applabs.eventx.events.data.local.event

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Utsav Devadiga
 */

@Entity
data class EventEntity(
    val event_name: String,
    val event_timeStamp: String,
    val event_location: String,
    val event_duration: String,
    val event_description: String,
    val event_participants: String,
    val category: String,
    @PrimaryKey(autoGenerate = true)
    val event_id: Int = 0
)