package com.gft.initialization.framework

import android.util.Log
import com.gft.initialization.domain.model.ApplicationInitializationIdentifier
import com.gft.initialization.model.InitState
import com.gft.initialization.usecases.StreamInitializationStateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApplicationInitializationErrorReporter(
    streamInitializationState: StreamInitializationStateUseCase,
) {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            streamInitializationState(ApplicationInitializationIdentifier)
                .collect { state ->
                    if (state is InitState.Failed) {
                        Log.d("#Initialization", "Initialization failed", state.error)
                    }
                }
        }
    }
}
