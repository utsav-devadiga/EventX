package com.applabs.eventx.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.applabs.eventx.events.domain.repository.EventListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Utsav Devadiga
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val eventListRepository: EventListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

}