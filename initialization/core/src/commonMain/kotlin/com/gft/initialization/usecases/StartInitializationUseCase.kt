package com.gft.initialization.usecases

import com.gft.initialization.model.InitializationIdentifier
import com.gft.initialization.services.InitializationService

class StartInitializationUseCase {

    operator fun invoke(identifier: InitializationIdentifier) = InitializationService.initialize(identifier)
}
