package de.fridolin1.lecs4k

class TestVeloSystem: EntitySystem() {
    lateinit var entities: Collection<Entity>
    override fun addedToEngine() {
        entities = engine.family().all(PosTestComponent::class, VeloTestComponent::class).get().entities
    }

    override fun update(delta: Float) {
        for (entity in entities) {
            val velocity = entity.get(VeloTestComponent::class)!!
            val position = entity.get(PosTestComponent::class)!!
            position.x += velocity.vx * delta
            position.y += velocity.vy * delta
        }
    }
}