package de.fridolin1.lecs4k

import de.fridolin1.idCollection.MultiIDHolder
import de.fridolin1.idCollection.IDHolderCollection
import de.fridolin1.idCollection.IDMap
import kotlin.reflect.KClass

class Entity : MultiIDHolder() {
    private val components = IDMap<EntityComponent>()
    internal val families = IDHolderCollection<Family>()
    internal val engines = IDHolderCollection<Lecs4kEngine>()

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

    @Suppress("UNCHECKED_CAST")
    fun <T : EntityComponent> getComponent(componentClass: KClass<T>): T? = components[EntityComponent.getComponentID(componentClass)] as T?

    fun containsComponent(componentClass: KClass<out EntityComponent>): Boolean = components.containsID(EntityComponent.getComponentID(componentClass))

    private fun update() = engines.forEach { it.handleEntityUpdate(this) }
}