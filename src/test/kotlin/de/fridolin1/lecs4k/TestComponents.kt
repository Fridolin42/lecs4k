package de.fridolin1.lecs4k

import de.fridolin1.lecs4k.component.EntityComponent

data class PosTestComponent(var x: Float, var y: Float): EntityComponent()

data class VeloTestComponent(var vx: Float, var vy: Float): EntityComponent()

data class AccTestComponent(var ax: Float, var ay: Float): EntityComponent()



data class DimensionTestComponent(var width: Float, var height: Float): EntityComponent()

data class GrowTestComponent(var growFactor: Float): EntityComponent()