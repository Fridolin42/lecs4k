package de.fridolin1.lecs4k.component

import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.reflect.KClass

@OptIn(ExperimentalAtomicApi::class)
interface CompInterface {
    companion object {
        private val idCounter = AtomicInt(0)
        private val idMap = HashMap<KClass<out CompInterface>, Int>()
        internal fun getComponentID(c: KClass<out CompInterface>): Int {
            val id = idMap.getOrPut(c) { idCounter.fetchAndIncrement() }
            println("$c: $id")
            return id
        }
    }

    val componentID: Int
}