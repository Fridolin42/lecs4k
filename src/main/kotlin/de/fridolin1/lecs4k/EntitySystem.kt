package de.fridolin1.lecs4k

import de.fridolin1.idMap.DynamicID

abstract class EntitySystem: DynamicID() {
    lateinit var engine: Lecs4kEngine
        private set

    internal fun setEngine(engine: Lecs4kEngine) {
        if (this::engine.isInitialized) throw IllegalStateException("Already initialized")
        this.engine = engine
        this.addedToEngine()
    }

    abstract fun addedToEngine()

    abstract fun update(delta: Float)
}