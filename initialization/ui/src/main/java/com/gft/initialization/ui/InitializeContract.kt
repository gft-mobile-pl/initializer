package com.gft.initialization.ui

import androidx.annotation.RestrictTo
import com.gft.mvi.ViewState

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
sealed interface InitializeViewState : ViewState {
    data object Content : InitializeViewState
    data object Empty : InitializeViewState
    data class Error(val error: Throwable, val renderer: InitializationErrorRenderer?) : InitializeViewState
}
