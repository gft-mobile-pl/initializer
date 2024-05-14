package com.gft.initialization.domain.usecases

import com.gft.initialization.domain.services.InitializationService

class StreamInitializationStateUseCase(private val initializationService: InitializationService) {

    operator fun invoke() = initializationService.initState
}
