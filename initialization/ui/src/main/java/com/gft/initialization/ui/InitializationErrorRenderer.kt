package com.gft.initialization.ui

import androidx.compose.runtime.Composable

interface InitializationErrorRenderer {
    @Composable
    fun RenderError(error: Throwable, onRetry: () -> Unit)
    fun canRenderError(error: Throwable): Boolean
}
