package com.gft.initialization.di

import com.gft.initialization.domain.InitializerFive
import com.gft.initialization.domain.InitializerFour
import com.gft.initialization.domain.InitializerOne
import com.gft.initialization.domain.InitializerThree
import com.gft.initialization.domain.InitializerTwo
import com.gft.initialization.domain.model.initializeInParallel
import org.koin.dsl.module

val appModule = module {

    factory {
        listOf(
            { InitializerOne() },
            { InitializerTwo() },
            {
                initializeInParallel(
                    { InitializerThree() },
                    { InitializerFour() },
                )
            },
            { InitializerFive() }
        )
    }
}
