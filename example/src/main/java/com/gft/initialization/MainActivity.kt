package com.gft.initialization

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gft.initialization.di.AppInitializationErrorRenderers
import com.gft.initialization.domain.model.ApplicationInitializationIdentifier
import com.gft.initialization.ui.Initialize
import com.gft.initialization.ui.initializationprogressindicator.CustomSplashScreen
import com.gft.initialization.ui.theme.InitializerTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InitializerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Initialize(
                        initializationIdentifier = ApplicationInitializationIdentifier,
                        showContentDuringInitialization = true,
                        errorRenderersProvider = get(AppInitializationErrorRenderers)
                    ) {

                        CustomSplashScreen(
                            initializationIdentifier = ApplicationInitializationIdentifier
                        ) {
                            ApplicationContent(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Application Content",
            color = MaterialTheme.colorScheme.primary
        )
    }
}
