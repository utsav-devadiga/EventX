package com.applabs.eventx.events.data.repository

import com.applabs.eventx.events.data.local.event.EventDatabase
import com.applabs.eventx.events.data.local.event.EventEntity
import com.applabs.eventx.events.data.mappers.toEvent
import com.applabs.eventx.events.domain.model.Event
import com.applabs.eventx.events.domain.repository.EventListRepository
import com.applabs.eventx.events.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author Utsav Devadiga
 */
class EventListRepositoryImpl @Inject constructor(
    private val eventDatabase: EventDatabase
) : EventListRepository {

    override suspend fun getEventList(
        forceFetchFromRemote: Boolean
    ): Flow<Resource<List<Event>>> {
        return flow {
            //start the flow by loading
            emit(Resource.Loading(true))

            val localEventList = eventDatabase.eventDao.getAllEvents()

            //this flag can be used to scale the app to connect your api / backend
            //as we are not using any REST API will force this flag to false
            /** localEventList.isNotEmpty() && !forceFetchFromRemote  **/
            val shouldLoadLocalEvents = true


            if (shouldLoadLocalEvents) {
                emit(
                    Resource.Success(
                        data = localEventList.map { eventEntity ->
                            eventEntity.toEvent()
                        }
                    )
                )

                emit(Resource.Loading(false))
                return@flow
            }

            /** here we continue with remote fetching  **/


        }
    }

    override suspend fun getEvent(id: Int): Flow<Resource<Event>> {

        return flow {

            emit(Resource.Loading(true))

            val eventEntity = eventDatabase.eventDao.getEventById(id)

            if (eventEntity != null) {
                emit(
                    Resource.Success(
                        eventEntity.toEvent()
                    )
                )

                emit(
                    Resource.Loading(
                        false
                    )
                )

                return@flow

            }

            emit(
                Resource.Error(
                    "Error: No Such Event"
                )
            )

            emit(
                Resource.Loading(
                    false
                )
            )


        }

    }

    override suspend fun addSampleEvent(): Boolean {

        val event = EventEntity(
            event_description = "test",
            event_location = "test location",
            event_duration = "3 days",
            event_participants = "test,test2,test3",
            event_timeStamp = "xxxxx",
            event_name = "event Name",
            category = "test",
            event_id = 1
        )

        eventDatabase.eventDao.upsertEvent(event = event)

        return true
    }


}