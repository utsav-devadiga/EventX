package com.applabs.eventx.events.data.local.event

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


/**
 * @author Utsav Devadiga
 */
@Dao
interface EventDao {


    @Upsert
    suspend fun upsertEvent(event: EventEntity)


    @Query("SELECT * FROM EventEntity WHERE event_id = :event_id")
    suspend fun getEventById(event_id: Int): EventEntity

    @Query("SELECT * FROM EventEntity")
    suspend fun getAllEvents(): List<EventEntity>


}