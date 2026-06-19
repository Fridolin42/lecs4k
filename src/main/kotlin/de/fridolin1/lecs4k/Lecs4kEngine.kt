package de.fridolin1.lecs4k

import de.fridolin1.idMap.DynamicID
import de.fridolin1.idMap.DynamicIdCollection
import de.fridolin1.lecs4k.listener.EntityListener
import kotlin.reflect.KClass

class Lecs4kEngine : DynamicID() {
    private val entities = DynamicIdCollection<Entity>()
    private val families = DynamicIdCollection<Family>()
    private val systems = DynamicIdCollection<EntitySystem>()

    val listeners = ArrayList<EntityListener>()

    fun family(all: List<KClass<EntityComponent>>, one: List<KClass<EntityComponent>>, none: List<KClass<EntityComponent>>): Family {
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

    fun createEntity(entity: Entity): Entity {
        val entity = Entity()
        addEntity(entity)
        return entity
    }

    fun addEntity(entity: Entity) {
        entity.engines.add(this)
        entities.add(entity)
        handleEntityUpdate(entity)
    }

    fun removeEntity(entity: Entity) {
        entity.engines.remove(this)
        entities.remove(entity)
        removeEntityFromFamilies(entity)
    }

    internal fun handleEntityUpdate(entity: Entity) {
        for (family in families) {
            family.entityUpdate(entity)
        }
    }

    private fun removeEntityFromFamilies(entity: Entity) {
        for (family in entity.families) family.removeEntity(entity)
    }
}