package com.applabs.eventx.events.data.repository

import android.util.Log
import com.applabs.eventx.events.data.local.event.EventDatabase
import com.applabs.eventx.events.data.local.event.EventEntity
import com.applabs.eventx.events.data.mappers.toEvent
import com.applabs.eventx.events.data.mappers.toEventEntity
import com.applabs.eventx.events.data.remote.response.EventDto
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


    override suspend fun addEvent(event: EventDto): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))

            val eventEntity = event.toEventEntity()
            val eventStatus: Long = eventDatabase.eventDao.insertEvent(eventEntity)

            if (eventStatus != -1L) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Error: Unable to insert event"))
            }

            emit(Resource.Loading(false))

        }
    }

    override suspend fun editEvent(event: EventDto) {
        val eventEntity = event.toEventEntity()
        eventDatabase.eventDao.updateEvent(eventEntity)
    }

}