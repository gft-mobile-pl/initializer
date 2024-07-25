package com.gft.initialization.domain.initializers

import android.util.Log
import com.gft.initialization.model.Initializer
import kotlinx.coroutines.delay

class InitializerOne : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "Initializer One: start.")
        delay(500)
        Log.d("#Initialization", "Initializer One: end.")
    }
}
