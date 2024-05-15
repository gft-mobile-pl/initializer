package com.gft.initialization.domain

import android.util.Log
import com.gft.initialization.domain.model.Initializer
import kotlinx.coroutines.delay

class InitializerSix : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "Initializer Six: start.")
        delay(500)
        throw RuntimeException("InitializerSix failed.")
    }
}
