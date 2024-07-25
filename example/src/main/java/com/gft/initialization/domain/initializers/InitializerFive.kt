package com.gft.initialization.domain.initializers

import android.util.Log
import com.gft.initialization.model.Initializer
import kotlinx.coroutines.delay

class InitializerFive : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "Initializer Five: start.")
        delay(500)
        Log.d("#Initialization", "Initializer Five: end.")
    }
}
