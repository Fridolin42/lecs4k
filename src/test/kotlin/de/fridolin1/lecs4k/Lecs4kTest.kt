package de.fridolin1.lecs4k

import de.fridolin1.lecs4k.listener.EntityListener
import de.fridolin1.lecs4k.listener.FamilyListener
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Lecs4kTest {
    lateinit var engine: Lecs4kEngine

    @BeforeEach
    fun setUp() {
        engine = Lecs4kEngine()
    }

    @Test
    fun entityCreateDeleteTest() {
        var entityAddCounter = 0
        var entityRemoveCounter = 0
        val entityListener = object : EntityListener() {
            override fun entityAdded(entity: Entity, engine: Lecs4kEngine) {
                entityAddCounter++
            }

            override fun entityRemoved(entity: Entity, engine: Lecs4kEngine) {
                entityRemoveCounter++
            }
        }
        engine.listeners.add(entityListener)

        val entity1 = engine.createEntity()
        val entity2 = engine.createEntity()
        assertTrue(engine.immutableEntityCollection.containsAll(listOf(entity1, entity2)))

        assertEquals(2, engine.immutableEntityCollection.size)
        engine.removeEntity(entity1)
        assertTrue(engine.immutableEntityCollection.contains(entity2))
        assertFalse(engine.immutableEntityCollection.contains(entity1))
        engine.removeEntity(entity2)
        assertTrue(engine.immutableEntityCollection.isEmpty())
        assertEquals(2, entityAddCounter)
        assertEquals(2, entityRemoveCounter)
    }

    @Test
    fun componentTest() {
        val entity1 = engine.createEntity()
        val entity2 = engine.createEntity()

        var componentAddCounter = 0
        var componentRemoveCounter = 0

        val entityListener = object : EntityListener() {
            override fun componentAdd(entity: Entity, component: EntityComponent, engine: Lecs4kEngine) {
                componentAddCounter++
            }

            override fun componentRemove(entity: Entity, component: EntityComponent, engine: Lecs4kEngine) {
                componentRemoveCounter++
            }
        }

        engine.listeners.add(entityListener)

        entity1.addComponent(PosTestComponent(1f, 2.5f))
        entity1.addComponent(VeloTestComponent(2f, 3f))
        entity2.addComponent(DimensionTestComponent(5f, 7f))
        entity2.addComponent(GrowTestComponent(1.1f))

        assertTrue(entity1.containsComponent(PosTestComponent::class))
        assertTrue(entity1.containsComponent(VeloTestComponent::class))
        assertFalse(entity1.containsComponent(AccTestComponent::class))
        assertFalse(entity1.containsComponent(DimensionTestComponent::class))
        assertFalse(entity1.containsComponent(GrowTestComponent::class))

        assertFalse(entity2.containsComponent(PosTestComponent::class))
        assertFalse(entity2.containsComponent(VeloTestComponent::class))
        assertFalse(entity2.containsComponent(AccTestComponent::class))
        assertTrue(entity2.containsComponent(DimensionTestComponent::class))
        assertTrue(entity2.containsComponent(GrowTestComponent::class))
        assertEquals(4, componentAddCounter)

        assertEquals(1f, entity1.getComponent(PosTestComponent::class)?.x)
        assertEquals(2.5f, entity1.getComponent(PosTestComponent::class)?.y)
        assertEquals(2f, entity1.getComponent(VeloTestComponent::class)?.vx)
        assertEquals(3f, entity1.getComponent(VeloTestComponent::class)?.vy)

        assertEquals(null, entity1.getComponent(DimensionTestComponent::class))
        assertEquals(null, entity1.getComponent(GrowTestComponent::class))

        assertEquals(5f, entity2.getComponent(DimensionTestComponent::class)?.width)
        assertEquals(7f, entity2.getComponent(DimensionTestComponent::class)?.height)
        assertEquals(1.1f, entity2.getComponent(GrowTestComponent::class)?.growFactor)

        assertEquals(null, entity2.getComponent(PosTestComponent::class))
        assertEquals(null, entity2.getComponent(VeloTestComponent::class))

        entity1.removeComponent(VeloTestComponent::class)
        entity2.removeComponent(GrowTestComponent::class)

        assertTrue(entity1.containsComponent(PosTestComponent::class))
        assertFalse(entity1.containsComponent(VeloTestComponent::class))
        assertTrue(entity2.containsComponent(DimensionTestComponent::class))
        assertFalse(entity2.containsComponent(GrowTestComponent::class))
        assertEquals(2, componentRemoveCounter)
        assertEquals(4, componentAddCounter)
    }

    @Test
    fun familyTest() {
        var familyAdds = 0
        var familyRemoves = 0
        val family = engine.family(listOf(PosTestComponent::class, VeloTestComponent::class), none = listOf(GrowTestComponent::class))
        family.listeners.add(object : FamilyListener() {
            override fun entityAdded(entity: Entity, engine: Lecs4kEngine) {
                familyAdds++
            }

            override fun entityRemoved(entity: Entity, engine: Lecs4kEngine) {
                familyRemoves++
            }
        })
        val entities = mutableListOf<Entity>()
        repeat(16) {
            entities.add(engine.createEntity())
        }
        assertEquals(16, engine.immutableEntityCollection.size)

        entities[5].addComponent(GrowTestComponent(1.1f))
        entities[5].addComponent(PosTestComponent(5f, 7f))
        entities[5].addComponent(VeloTestComponent(1f, 2f))

        entities[11].addComponent(PosTestComponent(4f, 15f))
        entities[11].addComponent(VeloTestComponent(4f, 2f))

        entities[15].addComponent(PosTestComponent(9f, 5f))
        entities[15].addComponent(VeloTestComponent(2f, 1f))
        entities[15].addComponent(GrowTestComponent(3.14f))

        entities[7].addComponent(PosTestComponent(42f, 21f))
        entities[7].addComponent(VeloTestComponent(1337f, 314f))

        assertEquals(familyAdds, 3)
        assertEquals(familyRemoves, 1)
        assertEquals(family.immutableEntityCollection.size, 2)
        repeat(16) {
            if (it == 11 || it == 7) assertTrue(family.immutableEntityCollection.contains(entities[it]))
            else assertFalse(family.immutableEntityCollection.contains(entities[it]))
        }

        engine.removeEntity(entities[11])

        assertEquals(2, familyRemoves)
        assertFalse(family.immutableEntityCollection.contains(entities[11]))
    }

    @Test
    fun systemTest() {
        engine.addSystem(TestVeloSystem())
        val entity1 = engine.createEntity()
        val entity2 = engine.createEntity()

        entity1.addComponent(PosTestComponent(1f, 2f))
        entity1.addComponent(VeloTestComponent(2f, 3f))

        entity2.addComponent(PosTestComponent(-5f, -7f))
        entity2.addComponent(VeloTestComponent(0.2f, 0.5f))

        repeat(5) {
            engine.update(0.2f)
        }

        val pos1 = entity1.getComponent(PosTestComponent::class)!!
        val pos2 = entity2.getComponent(PosTestComponent::class)!!

        assertTrue(abs(3f - pos1.x) < 0.01)
        assertTrue(abs(5f - pos1.y) < 0.01)
        assertTrue(abs(-4.8f - pos2.x) < 0.01)
        assertTrue(abs(-6.5f - pos2.y) < 0.01)
    }
}