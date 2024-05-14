package com.gft.initialization.di

import com.gft.initialization.domain.services.InitializationService
import com.gft.initialization.domain.usecases.StartInitializationUseCase
import com.gft.initialization.domain.usecases.StreamInitializationStateUseCase
import com.gft.initialization.ui.InitializeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val InitializersQualifier = named("com.gft.initialization.model.Initializer")
val InitializationErrorRenderersQualifier = named("com.gft.initialization.ui.InitializationErrorRenderer")

val initializationModule = module {
    single { InitializationService(get(qualifier = InitializersQualifier)) }
    factoryOf(::StartInitializationUseCase)
    factoryOf(::StreamInitializationStateUseCase)
    viewModel { (showContentDuringInitialization: Boolean) ->
        InitializeViewModel(
            showContentDuringInitialization = showContentDuringInitialization,
            startInitialization = get(),
            streamInitializationState = get(),
            errorRenderersProvider = get(InitializationErrorRenderersQualifier)
        )
    }
}
