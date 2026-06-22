package de.fridolin1.lecs4k

import de.fridolin1.idCollection.MultiIDHolder
import de.fridolin1.idCollection.IDHolderCollection
import de.fridolin1.lecs4k.listener.EntityListener
import kotlin.reflect.KClass

class Lecs4kEngine : MultiIDHolder() {
    private val entities = IDHolderCollection<Entity>()
    val immutableEntityCollection = ImmutableCollection(entities)
    private val families = IDHolderCollection<Family>()
    private val systems = IDHolderCollection<EntitySystem>()

    val listeners = ArrayList<EntityListener>()

    fun family(all: List<KClass<out EntityComponent>> = listOf(), one: List<KClass<out EntityComponent>> = listOf(), none: List<KClass<out EntityComponent>> = listOf()): Family {
        if (entities.isNotEmpty()) throw IllegalStateException("There mustn't exists any entity in this engine to create a family")
        val family = Family(this, all, one, none)
        families.add(family)
        return family
    }

    fun update(delta: Float) {
        for (system in systems) {
            system.update(delta)
        }
    }

    fun addSystem(system: EntitySystem) {
        if (entities.isNotEmpty()) System.err.println("Lecs4k warn: You shouldn't register you systems when you already added entities to this engine (System: ${system::class.simpleName}")
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
        entities.add(entity)
        handleEntityUpdate(entity)
        listeners.forEach { it.entityAdded(entity, this) }
    }

    fun removeEntity(entity: Entity) {
        entity.engines.remove(this)
        entities.remove(entity)
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