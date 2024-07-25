package com.gft.initialization.domain.initializers

import android.util.Log
import com.gft.initialization.domain.model.VerySpecificError
import com.gft.initialization.model.Initializer
import kotlinx.coroutines.delay

class FailingInitializer : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "FailingInitializer: start.")
        delay(500)
        throw VerySpecificError()
    }
}
