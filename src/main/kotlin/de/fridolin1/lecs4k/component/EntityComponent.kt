package de.fridolin1.lecs4k.component

abstract class EntityComponent : CompInterface {
    override val componentID = CompInterface.getComponentID(this::class)
}