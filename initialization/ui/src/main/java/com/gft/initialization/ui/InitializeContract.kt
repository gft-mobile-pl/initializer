package com.gft.initialization.ui

import androidx.annotation.RestrictTo
import com.gft.mvi.ViewEffect
import com.gft.mvi.ViewEvent
import com.gft.mvi.ViewState

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
sealed interface InitializeViewState : ViewState {
    data object Content : InitializeViewState
    data object Empty : InitializeViewState
    data class Error(val error: Throwable, val renderer: InitializationErrorRenderer?) : InitializeViewState
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
sealed interface InitializeViewEvent : ViewEvent {
    data object OnRetryClicked : InitializeViewEvent
}

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
sealed interface InitializeViewEffect : ViewEffect {
    data object Restart : InitializeViewEffect
}
