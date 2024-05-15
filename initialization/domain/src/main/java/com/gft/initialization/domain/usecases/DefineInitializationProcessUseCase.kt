package com.gft.initialization.domain.usecases

import com.gft.initialization.domain.model.InitializationIdentifier
import com.gft.initialization.domain.model.Initializer
import com.gft.initialization.domain.services.InitializationService

class DefineInitializationProcessUseCase {
    operator fun invoke(
        identifier: InitializationIdentifier,
        initializers: List<() -> Initializer>,
    ) = InitializationService.defineInitializationProcess(identifier, initializers)
}
