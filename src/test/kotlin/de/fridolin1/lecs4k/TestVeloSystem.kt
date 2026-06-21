package de.fridolin1.lecs4k

class TestVeloSystem: EntitySystem() {
    lateinit var entities: Collection<Entity>
    override fun addedToEngine() {
        entities = engine.family(listOf(PosTestComponent::class, VeloTestComponent::class)).immutableEntityCollection
    }

    override fun update(delta: Float) {
        for (entity in entities) {
            val velocity = entity.getComponent(VeloTestComponent::class)!!
            val position = entity.getComponent(PosTestComponent::class)!!
            position.x += velocity.vx * delta
            position.y += velocity.vy * delta
        }
    }
}