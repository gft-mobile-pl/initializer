package com.gft.initialization.ui.utils

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

// TODO: remove this extensions once coroutines-utils library is created
internal fun <T, R> StateFlow<T>.mapStateFlow(mapper: (T) -> R): StateFlow<R> = MappedStateFlow(this, mapper)

private class MappedStateFlow<T, R>(
    private val source: StateFlow<T>,
    private val mapper: (T) -> R
) : StateFlow<R> {
    override val replayCache: List<R>
        get() = source.replayCache.map(mapper)

    override val value: R
        get() = mapper(source.value)

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
        source.collect { item -> collector.emit(mapper(item)) }
    }
}
