package com.example.nanameue_code_test.ui.create_post

import androidx.lifecycle.ViewModel
import com.example.nanameue_code_test.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

sealed class CreatePostEvent : NavigationEvent() {
    data object NavigateToTimeline : CreatePostEvent()
}

class CreatePostViewModel : ViewModel() {
    private val _navigationEvent = MutableSharedFlow<CreatePostEvent>(replay = 1)
    val navigationEvent: SharedFlow<CreatePostEvent> = _navigationEvent

    fun navigateToTimeline() {
        _navigationEvent.tryEmit(CreatePostEvent.NavigateToTimeline)
    }
}