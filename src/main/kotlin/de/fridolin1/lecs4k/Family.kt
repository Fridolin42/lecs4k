package de.fridolin1.lecs4k

import de.fridolin1.idMap.DynamicID
import de.fridolin1.idMap.DynamicIdCollection
import de.fridolin1.lecs4k.listener.FamilyListener
import kotlin.reflect.KClass

class Family : DynamicID {
    private val entities = DynamicIdCollection<Entity>()
    val immutableEntityCollection = ImmutableCollection(entities)
    val listeners = ArrayList<FamilyListener>()

    val engine: Lecs4kEngine
    val all: List<KClass<EntityComponent>>
    val one: List<KClass<EntityComponent>>
    val none: List<KClass<EntityComponent>>

    internal constructor(
        engine: Lecs4kEngine,
        all: List<KClass<EntityComponent>>,
        one: List<KClass<EntityComponent>>,
        none: List<KClass<EntityComponent>>
    ) {
        this.engine = engine
        this.all = all
        this.one = one
        this.none = none
    }

    fun entityUpdate(entity: Entity) {
        val status = all.all { entity.containsComponent(it) }
                && one.count { entity.containsComponent(it) } > 0
                && none.none { entity.containsComponent(it) }
        if (status && !entities.contains(entity)) {
            entities.add(entity)
            listeners.forEach { it.entityAdded(entity, engine) }
        } else if (!status && entities.contains(entity)) {
            entities.remove(entity)
            listeners.forEach { it.entityRemoved(entity, engine) }
        }
    }

    internal fun removeEntity(entity: Entity) {
        entities.remove(entity)
    }
}