package de.fridolin1.lecs4k

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.reflect.KClass

@OptIn(ExperimentalAtomicApi::class)
class EntityComponent {
    companion object {
        private val idCounter = AtomicInt(0)
        private val idMap = HashMap<KClass<out EntityComponent>, Int>()
        internal fun getComponentID(c: KClass<out EntityComponent>) = idMap.getOrPut(c) { idCounter.fetchAndIncrement() }
    }

    internal val componentID = getComponentID(this::class)
}