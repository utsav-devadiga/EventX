package com.applabs.eventx.events.presentation

/**
 * @author Utsav Devadiga
 */
sealed interface EventListUiEvent {

    object Navigate : EventListUiEvent
}