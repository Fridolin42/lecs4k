package de.fridolin1.lecs4k

import de.fridolin1.idMap.DynamicID
import de.fridolin1.idMap.DynamicIdCollection
import de.fridolin1.idMap.IdMap
import kotlin.reflect.KClass

class Entity : DynamicID() {
    private val components = IdMap<EntityComponent>()
    internal val families = DynamicIdCollection<Family>()
    internal val engines = DynamicIdCollection<Lecs4kEngine>()

    fun addComponent(component: EntityComponent) {
        engines.forEach { engine -> engine.listeners.forEach { listener -> listener.componentAdd(this, component, engine) } }
        components[component.componentID] = component
        update()
    }

    fun removeComponent(componentClass: KClass<out EntityComponent>) {
        val compID = EntityComponent.getComponentID(componentClass)
        val component = components[compID]
        components.remove(compID)
        update()
        if (component == null) return
        engines.forEach { engine -> engine.listeners.forEach { listener -> listener.componentRemove(this, component, engine) } }
    }

    fun getComponent(componentClass: KClass<out EntityComponent>): EntityComponent? = components[EntityComponent.getComponentID(componentClass)]

    fun containsComponent(componentClass: KClass<out EntityComponent>): Boolean = components.containsID(EntityComponent.getComponentID(componentClass))

    private fun update() = engines.forEach { it.handleEntityUpdate(this) }
}