package de.fridolin1.lecs4k

import de.fridolin1.lecs4k.component.CompInterface
import de.fridolin1.lecs4k.component.entityTag

enum class LivingTestTag : CompInterface by entityTag<LivingTestTag>() {
    LIVING, DEAD, DYING;
}