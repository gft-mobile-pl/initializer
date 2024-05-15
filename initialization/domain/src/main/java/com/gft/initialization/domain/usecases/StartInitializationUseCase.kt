package com.gft.initialization.domain.usecases

import com.gft.initialization.domain.model.InitializationIdentifier
import com.gft.initialization.domain.services.InitializationService

class StartInitializationUseCase {

    operator fun invoke(identifier: InitializationIdentifier) = InitializationService.initialize(identifier)
}
