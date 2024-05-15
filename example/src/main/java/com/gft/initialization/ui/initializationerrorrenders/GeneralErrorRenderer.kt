package com.gft.initialization.ui.initializationerrorrenders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gft.initialization.framework.usecases.RestartApplicationUseCase
import com.gft.initialization.ui.InitializationErrorRenderer

class GeneralErrorRenderer(
    private val restartApplication: RestartApplicationUseCase,
) : InitializationErrorRenderer {
    @Composable
    override fun RenderError(error: Throwable) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Initialization failed!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { restartApplication() }) {
                Text(text = "Try again")
            }
        }
    }

    override fun canRenderError(error: Throwable): Boolean = true
}
