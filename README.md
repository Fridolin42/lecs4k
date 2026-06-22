# Lecs4k

Entity component system (<b>ecs</b>) for Kotlin and Java

- [Setup](#setup)
- [Kotlin Tutorial](#tutorial-kotlin)
  - [create entities](#create-the-engine-and-an-entity)
  - [add components to your entity](#entitycomponents)
  - [how to process the entities](#entitysystems-and-families)
  - [listeners and life cycle](#listeners-life-cycle)
    - [entity listener](#for-entities)
    - [family listener](#for-families)
- [Java Tutorial (coming soon)](#tutorial-java)
- [Questions, contributions, Usage, etc](#questions-contributions-usage-etc)

## Setup

### Gradle

```kotlin
repositories {
    mavenCentral()
    maven {
      url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencies {
    implementation("de.fridolin1:lecs4k:0.4-SNAPSHOT")
}
```

## Tutorial (Kotlin)

### Create the engine and an entity

```kotlin
//the engine is the core of the ecs
val engine = Lecs4kEngine()

//create and register an entity in one step (recommended)
val entity = engine.createEntity()
//create and register an entity in two steps
val entity2 = Entity()
engine.addEntity(entity2)
```

### EntityComponents

```kotlin
//define a component
data class PositionComponent(val x: Float, val y: Float): EntityComponent()
```

```kotlin
//add the component
entity.addComponent(PositionComponent(4f, 2f))

//get the component
val position = entity.getComponent(PositionComponent::class)
println("Position: ${position.x}|${position.y}") //would print "Position: 4.0|2.0"

//remove a component
entity.removeComponent(PositionComponent::class)
println(entity.getComponent(PositionComponent::class)) //would print "null"
```


### EntitySystems and Families

Define your EntityComponents::

```kotlin
data class PositionComponent(val x: Float, val y: Float): EntityComponent()
data class VelocityComponent(val vx: Float, val vy: Float): EntityComponent()
```

Design your EntitySystem:

```kotlin
class MovementSystem: EntitySystem() {
    lateinit var entities: Collection<Entity>

    override fun addedToEngine() {
        entities = engine.family(listOf(PositionComponent::class, VelocityComponent::class)).immutableEntityCollection
    }

    override fun update(delta: Float) {
        for (entity in entities) {
            val velocity = entity.getComponent(VelocityComponent::class)!!
            val position = entity.getComponent(PositionComponent::class)!!
            position.x += velocity.vx * delta
            position.y += velocity.vy * delta
        }
    }
}
```

The engine.family() takes up to 3 parameters. Every entity, that matches these criteria are automatically in this family:

1. all: The entity must have all the specified components
2. one: The entity must have at least on of these components
3. none: The entity mustn't have these components

Note: If one of these parameters is empty, it will be ignored.

<br/>

Register the system and update the engine:

```kotlin
//create the engine and register the EntitySystem
val engine = Lecs4kEngine()
engine.addSystem(MovementSystem())

//create your entities and add the components
val entity = engine.createEntity()
entity.addComponent(PositionComponent(4f, 2f))
entity.addComponent(VelocityComponent(0.75f, 0.25f))

//let the engine process
//the delta parameter is the time in seconds since the last update.
val delta = 0.1
engine.update(delta)
//in libGDX, you can get your delta with:
val delta = Gdx.graphics.getDeltaTime()
engine.update(delta)
```

### Listeners (life cycle)

#### For Entities:

Define your listener:

```kotlin
class EntityEventHandler: EntityListener() {
    //Triggers before the entity is added to the engine 
    override fun entityAdded(entity: Entity, engine: Lecs4kEngine) {
        //Do cool stuff
    }
    //Triggers after the entity was removed from the engine
    override fun entityRemoved(entity: Entity, engine: Lecs4kEngine) {
        //Do cooler stuff
    }
    //Triggers before the component is added to the entity
    override fun componentAdd(entity: Entity, component: EntityComponent, engine: Lecs4kEngine) {
        //Do the coolest stuff
    }
    //Triggers after the component was removed from the entity
    override fun componentRemove(entity: Entity, component: EntityComponent, engine: Lecs4kEngine) {
        //but don't destroy physics with stuff that's as cool as 0 Kelvin  
    }
}
```

Register the listener:

```kotlin
engine.listeners.add(EntityEventHandler())
```

#### For Families:

Define your listener:

```kotlin
class FamilyUpdateHandler: FamilyListener() {
    //Triggers after the entity was added to the family
    override fun entityAdded(entity: Entity, engine: Lecs4kEngine) {
        //do nice stuff
    }

    //Triggers after the entity was removed from the family
    override fun entityRemoved(entity: Entity, engine: Lecs4kEngine) {
        //do even more nice stuff
    }
}
```

Register the listener:

```kotlin
val family = engine.family(listOf(PositionComponent::class, VelocityComponent::class))
family.listeners.add(FamilyUpdateHandler()) 
```

## Tutorial (Java)

<i>Coming soon. If you have a time machine, I would be glad if you can send me the tutorial for java that I'm going to put here.</i>

If you want to use Java but don't know anything about Kotlin, you can:
- check out this article: https://kotlinlang.org/docs/java-to-kotlin-interop.html
- ask the chatbot of your choice

Since Kotlin gets compiled to Java Byte Code too, these 2 languages are compatible.

## Questions, contributions, Usage, etc...

This project is under the lgpl license (see the "LICENSE" file). Therefore, you are free to copy and edit and share the project, as long as you keep the original license. Furtheremore, you are free to use this project as a libery in any commercial or closed-source project. <b>Read the license for more informations.</b>

If you have questions, feature requests or bug reports, please create an issue.

If you have to contact me for other reasons, use the email <a href="mailto:fridolin.dev@tuta.com">fridolin.dev@tuta.com</a>.
