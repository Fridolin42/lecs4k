package de.fridolin1.lecs4k.listener

import de.fridolin1.lecs4k.Entity
import de.fridolin1.lecs4k.Lecs4kEngine

abstract class FamilyListener {
    /** Triggered when entity was added to family */
    open fun entityAdded(entity: Entity, engine: Lecs4kEngine) {}

    /** Triggered when entity was removed from family */
    open fun entityRemoved(entity: Entity, engine: Lecs4kEngine) {}
}