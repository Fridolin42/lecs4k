package de.fridolin1.lecs4k

import de.fridolin1.idCollection.IDHolderCollection
import de.fridolin1.idCollection.IDMap
import de.fridolin1.idCollection.MultiIDHolder
import de.fridolin1.lecs4k.component.CompInterface
import kotlin.reflect.KClass

class Entity : MultiIDHolder() {
    private val components = IDMap<CompInterface>()
    internal val families = IDHolderCollection<Family>()
    internal val engines = IDHolderCollection<Lecs4kEngine>()

    fun add(component: CompInterface) {
        engines.forEach { engine -> engine.listeners.forEach { listener -> listener.componentAdd(this, component, engine) } }
        components[component.componentID] = component
        update()
    }

    fun remove(componentClass: KClass<out CompInterface>) {
        val compID = CompInterface.getComponentID(componentClass)
        val component = components[compID]
        components.remove(compID)
        update()
        if (component == null) return
        engines.forEach { engine -> engine.listeners.forEach { listener -> listener.componentRemove(this, component, engine) } }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : CompInterface> get(componentClass: KClass<T>): T? = components[CompInterface.getComponentID(componentClass)] as T?

    fun contains(componentClass: KClass<out CompInterface>): Boolean = components.containsID(CompInterface.getComponentID(componentClass))

    private fun update() = engines.forEach { it.handleEntityUpdate(this) }
}