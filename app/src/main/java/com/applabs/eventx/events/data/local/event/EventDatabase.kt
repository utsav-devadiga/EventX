package com.applabs.eventx.events.data.local.event

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author Utsav Devadiga
 */

@Database(
    entities = [EventEntity::class],
    version = 1
)
abstract class EventDatabase : RoomDatabase() {
    abstract val eventDao: EventDao
}
