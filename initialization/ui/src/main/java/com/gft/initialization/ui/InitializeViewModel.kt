package com.gft.initialization.ui

import androidx.annotation.RestrictTo
import com.gft.initialization.domain.model.InitState
import com.gft.initialization.domain.model.InitState.Failed
import com.gft.initialization.domain.model.InitState.Initializing
import com.gft.initialization.domain.model.InitState.NotInitialized
import com.gft.initialization.domain.model.InitializationIdentifier
import com.gft.initialization.domain.services.InitializationService
import com.gft.initialization.ui.InitializeViewEffect.Restart
import com.gft.initialization.ui.InitializeViewEvent.OnRetryClicked
import com.gft.initialization.ui.InitializeViewState.Content
import com.gft.initialization.ui.InitializeViewState.Empty
import com.gft.initialization.ui.InitializeViewState.Error
import com.gft.initialization.ui.utils.mapStateFlow
import com.gft.mvi.BaseMviViewModel
import com.gft.mvi.NavigationEffect
import kotlinx.coroutines.flow.StateFlow

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class InitializeViewModel(
    showContentDuringInitialization: Boolean,
    initializationIdentifier: InitializationIdentifier,
    errorRenderersProvider: () -> List<InitializationErrorRenderer>,
) : BaseMviViewModel<InitializeViewState, InitializeViewEvent, NavigationEffect, InitializeViewEffect>() {

    override val viewStates: StateFlow<InitializeViewState> = InitializationService.getInitializationState(initializationIdentifier)
        .mapStateFlow { state ->
            when (state) {
                NotInitialized, Initializing -> {
                    if (showContentDuringInitialization) Content else Empty
                }

                InitState.Initialized -> {
                    Content
                }

                is Failed -> {
                    var errorRenderer: InitializationErrorRenderer? = null
                    for (renderer in errorRenderersProvider()) {
                        if (renderer.canRenderError(state.error)) {
                            errorRenderer = renderer
                            break
                        }
                    }
                    Error(state.error, errorRenderer)
                }
            }
        }

    init {
        InitializationService.initialize(initializationIdentifier)
    }

    override fun onEvent(event: InitializeViewEvent) {
        when (event) {
            OnRetryClicked -> dispatchViewEffect(Restart)
        }
    }
}
