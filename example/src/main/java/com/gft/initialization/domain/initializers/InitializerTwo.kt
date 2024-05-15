package com.gft.initialization.domain.initializers

import android.util.Log
import com.gft.initialization.domain.model.Initializer
import kotlinx.coroutines.delay

class InitializerTwo : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "Initializer Two: start.")
        delay(500)
        Log.d("#Initialization", "Initializer Two: end.")
    }
}
