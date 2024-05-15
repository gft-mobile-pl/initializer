package com.gft.initialization.ui.initializationprogressindicator

import com.gft.mvi.ViewState

data class CustomSplashScreenViewState(
    internal val isLoadingIndicatorVisible: Boolean,
    internal val isContentVisible: Boolean,
) : ViewState
