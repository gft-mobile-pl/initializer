package com.gft.initialization.model

sealed interface InitState {
    data object NotInitialized : InitState
    data object Initializing : InitState
    data object Initialized : InitState
    class Failed(val error: Throwable) : InitState
}
