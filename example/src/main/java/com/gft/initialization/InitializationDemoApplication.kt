package com.gft.initialization

import android.app.Application
import com.gft.initialization.di.AppInitializersQualifier
import com.gft.initialization.di.appModule
import com.gft.initialization.domain.model.ApplicationInitializationIdentifier
import com.gft.initialization.domain.usecases.DefineInitializationProcessUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

class InitializationDemoApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InitializationDemoApplication)
            modules(appModule)
        }

        get<DefineInitializationProcessUseCase>()(
            identifier = ApplicationInitializationIdentifier,
            initializers = get(AppInitializersQualifier)
        )
    }
}
