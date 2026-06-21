package de.fridolin1.lecs4k

class ImmutableCollection<T>(val collection: Collection<T>) : Collection<T> {
    override val size
        get() = collection.size
    override fun isEmpty() = collection.isEmpty()
    override fun contains(element: T): Boolean = collection.contains(element)
    override fun iterator(): Iterator<T> = collection.iterator()
    override fun containsAll(elements: Collection<T>): Boolean = collection.containsAll(elements)
}