package com.gft.initialization.di

import com.gft.initialization.domain.initializers.FailingInitializer
import com.gft.initialization.domain.initializers.InitializerFive
import com.gft.initialization.domain.initializers.InitializerFour
import com.gft.initialization.domain.initializers.InitializerOne
import com.gft.initialization.domain.initializers.InitializerThree
import com.gft.initialization.domain.initializers.InitializerTwo
import com.gft.initialization.model.InitializationIdentifier
import com.gft.initialization.model.initializeInParallel
import com.gft.initialization.usecases.DefineInitializationProcessUseCase
import com.gft.initialization.usecases.StartInitializationUseCase
import com.gft.initialization.usecases.StreamInitializationStateUseCase
import com.gft.initialization.framework.ApplicationInitializationErrorReporter
import com.gft.initialization.framework.usecases.RestartApplicationUseCase
import com.gft.initialization.ui.initializationerrorrenders.GeneralErrorRenderer
import com.gft.initialization.ui.initializationerrorrenders.VerySpecificErrorRenderer
import com.gft.initialization.ui.initializationprogressindicator.CustomSplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppInitializersQualifier = named("AppInitializersQualifier")
val AppInitializationErrorRenderers = named("AppInitializationErrorRenderers")

val appModule = module {
    factoryOf(::DefineInitializationProcessUseCase)
    factoryOf(::StreamInitializationStateUseCase)
    factoryOf(::StartInitializationUseCase)

    factoryOf(::InitializerOne)
    factoryOf(::InitializerTwo)
    factoryOf(::InitializerThree)
    factoryOf(::InitializerFour)
    factoryOf(::InitializerFive)
    factoryOf(::FailingInitializer)

    viewModel { (initializationIdentifier: InitializationIdentifier) ->
        CustomSplashScreenViewModel(initializationIdentifier, get())
    }

    factory(qualifier = AppInitializersQualifier) {
        listOf(
            { get<InitializerOne>() },
            { get<InitializerTwo>() },
            {
                initializeInParallel(
                    { get<InitializerThree>() },
                    { get<InitializerFour>() }
                )
            },
            { get<InitializerFive>() },
            //{ get<FailingInitializer>() } // Uncomment to test initialization error.
        )
    }

    factoryOf(::VerySpecificErrorRenderer)
    factoryOf(::GeneralErrorRenderer)
    factory(qualifier = AppInitializationErrorRenderers) {
        {
            listOf(
                get<VerySpecificErrorRenderer>(),
                get<GeneralErrorRenderer>()
            )
        }
    }

    singleOf(::ApplicationInitializationErrorReporter) {
        createdAtStart()
    }

    factoryOf(::RestartApplicationUseCase)
}
