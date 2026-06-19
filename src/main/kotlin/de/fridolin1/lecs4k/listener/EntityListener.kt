package de.fridolin1.lecs4k.listener

import de.fridolin1.lecs4k.Entity
import de.fridolin1.lecs4k.EntityComponent
import de.fridolin1.lecs4k.Lecs4kEngine

interface EntityListener {
    /** Triggered when entity was added to the engine */
    fun entityAdded(entity: Entity, engine: Lecs4kEngine)

    /** Triggered when entity was removed from engine */
    fun entityRemoved(entity: Entity, engine: Lecs4kEngine)

    /** Triggered before component is added to entity */
    fun componentAdd(entity: Entity, component: EntityComponent, engine: Lecs4kEngine)

    /** Triggered after component is removed from entity */
    fun componentRemove(entity: Entity, component: EntityComponent, engine: Lecs4kEngine)
}