package com.gft.initialization.ui.initializationprogressindicator

import androidx.lifecycle.viewModelScope
import com.gft.initialization.domain.model.InitState
import com.gft.initialization.domain.model.InitState.Initialized
import com.gft.initialization.domain.model.InitializationIdentifier
import com.gft.initialization.domain.usecases.StreamInitializationStateUseCase
import com.gft.mvi.BaseMviViewModel
import com.gft.mvi.NavigationEffect
import com.gft.mvi.ViewEffect
import com.gft.mvi.ViewEvent
import com.gft.mvi.coroutines.toViewStates
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class CustomSplashScreenViewModel(
    initializationIdentifier: InitializationIdentifier,
    streamInitializationState: StreamInitializationStateUseCase,
) : BaseMviViewModel<CustomSplashScreenViewState, ViewEvent, NavigationEffect, ViewEffect>() {

    override val viewStates: StateFlow<CustomSplashScreenViewState> = streamInitializationState(initializationIdentifier)
        .map { state ->
            state.toViewState()
        }
        .toViewStates(
            streamInitializationState(initializationIdentifier).value.toViewState(),
            viewModelScope
        )

    override fun onEvent(event: ViewEvent) = Unit

    private fun InitState.toViewState() = CustomSplashScreenViewState(
        isLoadingIndicatorVisible = this != Initialized,
        isContentVisible = this == Initialized
    )
}
