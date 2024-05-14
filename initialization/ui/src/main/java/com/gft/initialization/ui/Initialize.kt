package com.gft.initialization.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.gft.initialization.ui.InitializeViewEffect.Restart
import com.gft.initialization.ui.InitializeViewEvent.OnRetryClicked
import com.gft.initialization.ui.InitializeViewState.Content
import com.gft.initialization.ui.InitializeViewState.Empty
import com.gft.initialization.ui.InitializeViewState.Error
import com.gft.mvi.MviViewModel
import com.gft.mvi.NavigationEffect
import com.gft.mvi.ViewEffect
import com.gft.mvi.ViewState

@Composable
fun Initialize(
    viewModel: MviViewModel<InitializeViewState, InitializeViewEvent, NavigationEffect, InitializeViewEffect>,
    onRestart: () -> Unit,
    content: @Composable () -> Unit,
) {
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
