package com.gft.initialization.domain.services

import com.gft.initialization.domain.model.InitState
import com.gft.initialization.domain.model.InitState.Initialized
import com.gft.initialization.domain.model.InitState.Initializing
import com.gft.initialization.domain.model.InitState.NotInitialized
import com.gft.initialization.domain.model.InitializationIdentifier
import com.gft.initialization.domain.model.Initializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object InitializationService {
    private val definitions = hashMapOf<InitializationIdentifier, InitializationDefinition>()

    fun defineInitializationProcess(
        identifier: InitializationIdentifier,
        initializers: List<() -> Initializer>,
    ) {
        if (definitions.containsKey(identifier)) {
            throw IllegalArgumentException("There is another initialization process defined already with the provided identifier.")
        }

        definitions[identifier] = InitializationDefinition(initializers)
    }

    fun initialize(identifier: InitializationIdentifier) {
        val definition =
            definitions[identifier] ?: throw IllegalArgumentException("There is no initialization process defined with the provided identifier.")
        if (definition.state.value != NotInitialized) return

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

    fun getInitializationState(identifier: InitializationIdentifier): StateFlow<InitState> =
        definitions[identifier]?.state ?: throw IllegalArgumentException("There is no initialization process defined with the provided identifier.")

    private class InitializationDefinition(
        val initializers: List<() -> Initializer>,
        val state: MutableStateFlow<InitState> = MutableStateFlow(NotInitialized),
    )
}
