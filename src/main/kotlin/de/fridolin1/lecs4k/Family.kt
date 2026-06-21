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
    val all: List<KClass<out EntityComponent>>
    val one: List<KClass<out EntityComponent>>
    val none: List<KClass<out EntityComponent>>

    internal constructor(
        engine: Lecs4kEngine,
        all: List<KClass<out EntityComponent>>,
        one: List<KClass<out EntityComponent>>,
        none: List<KClass<out EntityComponent>>
    ) {
        this.engine = engine
        this.all = all
        this.one = one
        this.none = none
    }

    fun entityUpdate(entity: Entity) {
        val fitInFamily = (all.isEmpty() || all.all { entity.containsComponent(it) })
                && (one.isEmpty() || one.count { entity.containsComponent(it) } > 0)
                && (none.isEmpty() || none.none { entity.containsComponent(it) })
        val isInFamily = entities.contains(entity)
        if (fitInFamily && !isInFamily) {
            entity.families.add(this)
            entities.add(entity)
            listeners.forEach { it.entityAdded(entity, engine) }
        } else if (!fitInFamily && isInFamily) {
            entities.remove(entity)
            entity.families.remove(this)
            listeners.forEach { it.entityRemoved(entity, engine) }
        }
    }

    internal fun removeEntity(entity: Entity) {
        entities.remove(entity)
        entity.families.remove(this)
        listeners.forEach { it.entityRemoved(entity, engine) }
    }
}