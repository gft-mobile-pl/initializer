package com.gft.initialization.domain.services

import com.gft.initialization.domain.model.InitState
import com.gft.initialization.domain.model.InitState.Initialized
import com.gft.initialization.domain.model.InitState.Initializing
import com.gft.initialization.domain.model.InitState.NotInitialized
import com.gft.initialization.domain.model.Initializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InitializationService(
    private val initializers: List<() -> Initializer>,
) {
    private val _initState = MutableStateFlow<InitState>(NotInitialized)
    val initState: StateFlow<InitState> = _initState

    fun init() {
        CoroutineScope(Dispatchers.Unconfined).launch(start = CoroutineStart.UNDISPATCHED) {
            _initState.value = Initializing
            try {
                initializers.forEach { initializerProvider ->
                    initializerProvider().initialize()
                }
                _initState.value = Initialized
            } catch (error: Throwable) {
                _initState.value = InitState.Failed(error)
            }
        }
    }
}
