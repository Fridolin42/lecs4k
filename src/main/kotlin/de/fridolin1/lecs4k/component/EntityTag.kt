package de.fridolin1.lecs4k.component

import kotlin.reflect.KClass

open class EntityTag<T: CompInterface>(kClass: KClass<T>): CompInterface {
    override val componentID = CompInterface.getComponentID(kClass)
}

inline fun <reified T: CompInterface> entityTag() = EntityTag(T::class)
