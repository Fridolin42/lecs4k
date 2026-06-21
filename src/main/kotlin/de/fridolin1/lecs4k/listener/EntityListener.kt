package de.fridolin1.lecs4k.listener

import de.fridolin1.lecs4k.Entity
import de.fridolin1.lecs4k.EntityComponent
import de.fridolin1.lecs4k.Lecs4kEngine

abstract class EntityListener {
    /** Triggered when entity was added to the engine */
    open fun entityAdded(entity: Entity, engine: Lecs4kEngine) {}

    /** Triggered when entity was removed from engine */
    open fun entityRemoved(entity: Entity, engine: Lecs4kEngine) {}

    /** Triggered before component is added to entity */
    open fun componentAdd(entity: Entity, component: EntityComponent, engine: Lecs4kEngine) {}

    /** Triggered after component is removed from entity */
    open fun componentRemove(entity: Entity, component: EntityComponent, engine: Lecs4kEngine) {}
}