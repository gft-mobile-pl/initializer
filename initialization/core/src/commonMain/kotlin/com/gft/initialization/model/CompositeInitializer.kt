package com.gft.initialization.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

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
                            Result.success(Unit)
                        } catch (error: Throwable) {
                            Result.failure(error)
                        }
                    }
                }
                .awaitAll()
                .forEach { result ->
                    if (result.isFailure) throw result.exceptionOrNull()!!
                }
        }
    }
}

fun initializeInParallel(vararg initializers: () -> Initializer): Initializer = CompositeInitializer(initializers.toList())
