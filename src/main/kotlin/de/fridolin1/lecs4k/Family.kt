package de.fridolin1.lecs4k

import de.fridolin1.idCollection.MultiIDHolder
import de.fridolin1.idCollection.IDHolderCollection
import de.fridolin1.lecs4k.listener.FamilyListener
import kotlin.reflect.KClass

class Family : MultiIDHolder {
    private val internalEntities = IDHolderCollection<Entity>()
    val entities = ImmutableCollection(internalEntities)
    private val listeners = ArrayList<FamilyListener>()

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

    fun addListener(listener: FamilyListener) = listeners.add(listener)

    internal fun entityUpdate(entity: Entity) {
        val fitInFamily = (all.isEmpty() || all.all { entity.contains(it) })
                && (one.isEmpty() || one.count { entity.contains(it) } > 0)
                && (none.isEmpty() || none.none { entity.contains(it) })
        val isInFamily = internalEntities.contains(entity)
        if (fitInFamily && !isInFamily) {
            entity.families.add(this)
            internalEntities.add(entity)
            listeners.forEach { it.entityAdded(entity, engine) }
        } else if (!fitInFamily && isInFamily) {
            internalEntities.remove(entity)
            entity.families.remove(this)
            listeners.forEach { it.entityRemoved(entity, engine) }
        }
    }

    internal fun removeEntity(entity: Entity) {
        internalEntities.remove(entity)
        entity.families.remove(this)
        listeners.forEach { it.entityRemoved(entity, engine) }
    }
}