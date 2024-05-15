package com.gft.initialization.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gft.initialization.domain.model.InitializationIdentifier
import com.gft.initialization.ui.InitializeViewEffect.Restart
import com.gft.initialization.ui.InitializeViewEvent.OnRetryClicked
import com.gft.initialization.ui.InitializeViewState.Content
import com.gft.initialization.ui.InitializeViewState.Empty
import com.gft.initialization.ui.InitializeViewState.Error
import com.gft.mvi.ViewEffect
import com.gft.mvi.ViewState

@Composable
fun Initialize(
    initializationIdentifier: InitializationIdentifier,
    showContentDuringInitialization: Boolean,
    errorRenderersProvider: () -> List<InitializationErrorRenderer>,
    onRestart: () -> Unit,
    content: @Composable () -> Unit,
) {
    val viewModel = viewModel<InitializeViewModel>(
        factory = viewModelFactory {
            InitializeViewModel(showContentDuringInitialization, initializationIdentifier, errorRenderersProvider)
        }
    )

    ViewState(viewModel) {
        when (val state = viewState) {
            Content -> {
                content()
            }

            is Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    state.renderer?.RenderError(
                        error = state.error,
                        onRetry = remember {
                            {
                                viewModel.onEvent(OnRetryClicked)
                            }
                        }
                    )
                }
            }

            Empty -> {
                // Init is still in progress
            }
        }
    }

    ViewEffect(viewModel) { effect ->
        when (effect) {
            Restart -> onRestart()
        }
    }
}
