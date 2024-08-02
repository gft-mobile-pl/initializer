package com.gft.initialization.ui.initializationprogressindicator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.gft.initialization.model.InitializationIdentifier
import com.gft.mvi.MviViewModel
import com.gft.mvi.NavigationEffect
import com.gft.mvi.ViewEffect
import com.gft.mvi.ViewEvent
import com.gft.mvi.ViewState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CustomSplashScreen(
    initializationIdentifier: InitializationIdentifier,
    viewModel: MviViewModel<CustomSplashScreenViewState, ViewEvent, NavigationEffect, ViewEffect> = koinViewModel<CustomSplashScreenViewModel> {
        parametersOf(initializationIdentifier)
    },
    content: @Composable () -> Unit,
) {
    ViewState(viewModel) {
        if (viewState.isContentVisible) {
            content()
        }

        if (viewState.isLoadingIndicatorVisible) {
            Dialog(onDismissRequest = { }) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
