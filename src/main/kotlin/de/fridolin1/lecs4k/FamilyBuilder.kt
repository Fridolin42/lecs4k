package de.fridolin1.lecs4k

import de.fridolin1.lecs4k.component.CompInterface
import kotlin.reflect.KClass

class FamilyBuilder {
    val engine: Lecs4kEngine

    var all = mutableListOf<KClass<out CompInterface>>()
    var one = mutableListOf<KClass<out CompInterface>>()
    var none = mutableListOf<KClass<out CompInterface>>()

    internal constructor(engine: Lecs4kEngine) {
        this.engine = engine
    }

    fun all(vararg components: KClass<out CompInterface>): FamilyBuilder {
        all.addAll(components)
        return this
    }

    fun one(vararg components: KClass<out CompInterface>): FamilyBuilder {
        one.addAll(components)
        return this
    }

    fun none(vararg components: KClass<out CompInterface>): FamilyBuilder {
        none.addAll(components)
        return this
    }

    fun get(): Family {
        if (engine.internalEntities.isNotEmpty()) throw IllegalStateException("There mustn't exists any entity in this engine to create a family")
        val family = Family(engine, all, one, none)
        engine.families.add(family)
        return family
    }
}