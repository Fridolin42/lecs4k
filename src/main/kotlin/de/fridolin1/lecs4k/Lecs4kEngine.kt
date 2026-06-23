package de.fridolin1.lecs4k

import de.fridolin1.idCollection.IDHolderCollection
import de.fridolin1.idCollection.MultiIDHolder
import de.fridolin1.lecs4k.listener.EntityListener

class Lecs4kEngine : MultiIDHolder() {
    internal val internalEntities = IDHolderCollection<Entity>()
    val entities = ImmutableCollection(internalEntities)
    internal val families = IDHolderCollection<Family>()
    private val systems = IDHolderCollection<EntitySystem>()

    val listeners = ArrayList<EntityListener>()

    fun family(): FamilyBuilder = FamilyBuilder(this)

    fun update(delta: Float) {
        for (system in systems) {
            system.update(delta)
        }
    }

    fun addSystem(system: EntitySystem) {
        if (internalEntities.isNotEmpty()) System.err.println("Lecs4k warn: You shouldn't register you systems when you already added entities to this engine (System: ${system::class.simpleName}")
        systems.add(system)
        system.setEngine(this)
    }

    fun createEntity(): Entity {
        val entity = Entity()
        addEntity(entity)
        return entity
    }

    fun addEntity(entity: Entity) {
        entity.engines.add(this)
        internalEntities.add(entity)
        handleEntityUpdate(entity)
        listeners.forEach { it.entityAdded(entity, this) }
    }

    fun removeEntity(entity: Entity) {
        entity.engines.remove(this)
        internalEntities.remove(entity)
        removeEntityFromFamilies(entity)
        listeners.forEach { it.entityRemoved(entity, this) }
    }

    internal fun handleEntityUpdate(entity: Entity) {
        for (family in families) {
            family.entityUpdate(entity)
        }
    }

    private fun removeEntityFromFamilies(entity: Entity) {
        while (entity.families.isNotEmpty()) entity.families.get(0).removeEntity(entity)
    }
}