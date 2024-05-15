package com.gft.initialization.domain

import android.util.Log
import com.gft.initialization.domain.model.Initializer
import kotlinx.coroutines.delay

class InitializerThree : Initializer {
    override suspend fun initialize() {
        Log.d("#Initialization", "Initializer Three: start.")
        delay(2500)
        Log.d("#Initialization", "Initializer Three: end.")
    }
}
