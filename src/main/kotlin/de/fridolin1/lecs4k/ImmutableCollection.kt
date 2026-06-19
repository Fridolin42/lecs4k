package de.fridolin1.lecs4k

class ImmutableCollection<T>(val list: Collection<T>) : Collection<T> {
    override val size = list.size
    override fun isEmpty() = list.isEmpty()
    override fun contains(element: T): Boolean = list.contains(element)
    override fun iterator(): Iterator<T> = list.iterator()
    override fun containsAll(elements: Collection<T>): Boolean = list.containsAll(elements)
}