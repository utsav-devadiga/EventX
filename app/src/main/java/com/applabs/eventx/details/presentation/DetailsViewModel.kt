package com.applabs.eventx.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class DetailsViewModel @Inject constructor(
    private val eventListRepository: EventListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val eventId = savedStateHandle.get<Int>("eventId")

    private var _detialsState = MutableStateFlow(DetailsState())
    val detailsState = _detialsState.asStateFlow()

    init {
        getEvent(eventId ?: -1)
    }

    private fun getEvent(id: Int) {
        viewModelScope.launch {
            _detialsState.update {
                it.copy(isLoading = true)
            }

            eventListRepository.getEvent(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detialsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detialsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detialsState.update {
                                it.copy(event = movie)
                            }
                        }
                    }
                }
            }
        }
    }
}