package com.gft.initialization.usecases

import com.gft.initialization.model.InitializationIdentifier
import com.gft.initialization.model.Initializer
import com.gft.initialization.services.InitializationService

class DefineInitializationProcessUseCase {
    operator fun invoke(
        identifier: InitializationIdentifier,
        initializers: List<() -> Initializer>,
    ) = InitializationService.defineInitializationProcess(identifier, initializers)
}
