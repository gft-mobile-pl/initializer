package com.gft.initialization.domain.usecases

import com.gft.initialization.domain.model.InitState.NotInitialized
import com.gft.initialization.domain.services.InitializationService

class StartInitializationUseCase(private val initializationService: InitializationService) {

    operator fun invoke() {
        if (initializationService.initState.value == NotInitialized) {
            initializationService.init()
        }
    }
}
