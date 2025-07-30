package com.gft.initialization.services

import com.gft.initialization.model.InitState
import com.gft.initialization.model.InitState.Initialized
import com.gft.initialization.model.InitState.Initializing
import com.gft.initialization.model.InitState.NotInitialized
import com.gft.initialization.model.InitializationIdentifier
import com.gft.initialization.model.Initializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

object InitializationService {
    private val definitions = hashMapOf<InitializationIdentifier, InitializationDefinition>()
    private val onInitializationDefined = MutableStateFlow<InitializationIdentifier?>(null)

    fun defineInitializationProcess(
        identifier: InitializationIdentifier,
        initializers: List<() -> Initializer>,
    ) {
        if (definitions.containsKey(identifier)) {
            throw IllegalArgumentException("There is another initialization process defined already with the provided identifier.")
        }

        definitions[identifier] = InitializationDefinition(initializers)
        onInitializationDefined.value = identifier
    }

    fun initialize(identifier: InitializationIdentifier) {
        val definition =
            definitions[identifier] ?: throw IllegalArgumentException("There is no initialization process defined with the provided identifier.")
        if (definition.state.value == Initializing || definition.state.value == Initialized) return

        CoroutineScope(Dispatchers.Unconfined).launch(start = CoroutineStart.UNDISPATCHED) {
            definition.state.value = Initializing
            try {
                definition.initializers.forEach { initializerProvider ->
                    initializerProvider().initialize()
                }
                definition.state.value = Initialized
            } catch (error: Throwable) {
                definition.state.value = InitState.Failed(error)
            }
        }
    }

    fun getInitializationState(identifier: InitializationIdentifier): StateFlow<InitState> = object : StateFlow<InitState> {
        private val initializationState: StateFlow<InitState>?
            get() = definitions[identifier]?.state

        override val replayCache: List<InitState> = initializationState?.replayCache ?: listOf(NotInitialized)

        override val value: InitState
            get() = initializationState?.value ?: NotInitialized

        override suspend fun collect(collector: FlowCollector<InitState>): Nothing {
            (initializationState ?: onInitializationDefined.mapNotNull { initializationState }.first()).collect(collector)
        }
    }


    private class InitializationDefinition(
        val initializers: List<() -> Initializer>,
        val state: MutableStateFlow<InitState> = MutableStateFlow(NotInitialized),
    )
}
