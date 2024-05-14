package com.gft.initialization.domain.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.Optional

private class CompositeInitializer(
    private val initializers: List<() -> Initializer>,
) : Initializer {

    override suspend fun initialize() {
        coroutineScope {
            // we need to wait for all initializers to complete to throw errors in the same order as initializers are registered
            initializers
                .map { initializer ->
                    async {
                        try {
                            withContext(Dispatchers.IO) {
                                initializer().initialize()
                            }
                            Optional.empty<Throwable>()
                        } catch (error: Throwable) {
                            Optional.of(error)
                        }
                    }
                }
                .awaitAll()
                .forEach { error ->
                    if (error.isPresent) throw error.get()
                }
        }
    }
}

fun initializeInParallel(vararg initializers: () -> Initializer): Initializer = CompositeInitializer(initializers.toList())
