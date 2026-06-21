package de.fridolin1.lecs4k

import de.fridolin1.idCollection.MultiIDHolder

abstract class EntitySystem: MultiIDHolder() {
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