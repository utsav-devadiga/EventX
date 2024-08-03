package com.applabs.eventx.events.domain.repository

import com.applabs.eventx.events.domain.model.Event
import com.applabs.eventx.events.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * @author Utsav Devadiga
 */
interface EventListRepository {


    suspend fun getEventList(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<List<Event>>>


    suspend fun getEvent(id: Int): Flow<Resource<Event>>

    suspend fun addSampleEvent(): Boolean

}