package com.gft.initialization.usecases

import com.gft.initialization.model.InitializationIdentifier
import com.gft.initialization.services.InitializationService

class StreamInitializationStateUseCase {

    operator fun invoke(identifier: InitializationIdentifier) = InitializationService.getInitializationState(identifier)
}
