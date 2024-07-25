package com.gft.initialization.domain.initializers

import android.util.Log
import com.gft.initialization.model.Initializer
import kotlinx.coroutines.delay

class InitializerFour : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "Initializer Four: start.")
        delay(1000)
        Log.d("#Initialization", "Initializer Four: end.")
    }
}
