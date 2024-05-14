package com.gft.initialization.ui

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun Initialize(
    showContentDuringInitialization: Boolean,
    onRestart: () -> Unit,
    content: @Composable () -> Unit,
) {
    Initialize(
        viewModel = koinViewModel<InitializeViewModel> { parametersOf(showContentDuringInitialization) },
        onRestart = onRestart,
        content = content,
    )
}
