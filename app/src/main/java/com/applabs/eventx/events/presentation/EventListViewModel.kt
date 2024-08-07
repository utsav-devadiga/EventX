package com.applabs.eventx.events.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applabs.eventx.events.data.remote.response.EventDto
import com.applabs.eventx.events.domain.repository.EventListRepository
import com.applabs.eventx.events.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Utsav Devadiga
 */
@HiltViewModel
class EventListViewModel @Inject constructor(
    private val eventListRepository: EventListRepository
) : ViewModel() {

    private var _eventListState = MutableStateFlow(EventListState())

    val eventListState = _eventListState.asStateFlow()

    init {
        getAllEvents(false)

        addSampleEvent()
    }


    fun onEvent(event: EventListUiEvent) {
        when (event) {
            EventListUiEvent.Navigate -> {
                _eventListState.update {
                    it.copy(
                        isCurrentPageDashboard = !eventListState.value.isCurrentPageDashboard
                    )
                }
            }

        }
    }


    private fun getAllEvents(fetchFromRemote: Boolean) {

        //getting the all events via coroutines
        //viewmodel scope
        viewModelScope.launch {
            _eventListState.update {
                it.copy(isLoading = true)
            }

            eventListRepository.getEventList(fetchFromRemote)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> _eventListState.update {
                            it.copy(isLoading = false)
                        }

                        is Resource.Loading -> _eventListState.update {
                            it.copy(isLoading = true)
                        }

                        is Resource.Success -> result.data?.let { eventList ->
                            _eventListState.update {
                                it.copy(
                                    eventList = eventList,
                                )
                            }

                            Log.d("VIEWMODEL", "getAllEvents: ${eventList}")
                        }

                    }
                }
        }

    }

    fun addEvents(event: EventDto) {
        viewModelScope.launch {
            Log.d("DATABASE OPERATION", "addEvent: from viewmodel")
            eventListRepository.addEvent(event)
            getAllEvents(fetchFromRemote = false) // Refresh the event list after adding a new event
        }
    }

    fun editEvent(event: EventDto) {
        viewModelScope.launch {
            eventListRepository.editEvent(event)
            getAllEvents(fetchFromRemote = false) // Refresh the event list after editing an event
        }
    }


    private fun addSampleEvent() {
        viewModelScope.launch {
            eventListRepository.addSampleEvent()
        }
    }

}
