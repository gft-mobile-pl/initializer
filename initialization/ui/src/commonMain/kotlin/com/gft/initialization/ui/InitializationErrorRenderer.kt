package com.gft.initialization.ui

import androidx.compose.runtime.Composable

interface InitializationErrorRenderer {
    @Composable
    fun RenderError(error: Throwable)
    fun canRenderError(error: Throwable): Boolean
}
