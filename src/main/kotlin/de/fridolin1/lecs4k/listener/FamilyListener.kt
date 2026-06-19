package de.fridolin1.lecs4k.listener

import de.fridolin1.lecs4k.Entity
import de.fridolin1.lecs4k.Lecs4kEngine

interface FamilyListener {
    /** Triggered when entity was added to family */
    fun entityAdded(entity: Entity, engine: Lecs4kEngine)

    /** Triggered when entity was removed from family */
    fun entityRemoved(entity: Entity, engine: Lecs4kEngine)
}