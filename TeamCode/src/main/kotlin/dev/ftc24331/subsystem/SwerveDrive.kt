package dev.ftc24331.subsystem

import dev.ftc24331.util.Periodic
import org.joml.Vector2d
import kotlin.collections.forEachIndexed
import kotlin.math.atan2
import kotlin.math.hypot

class SwerveDrive(
    val modules: List<SwerveModule>,
    private val modulePositions: List<Pair<Double, Double>>
): Periodic {
    init {
        require(modules.size == modulePositions.size) {
            "u must have the same number of modules provided thru the constructor as u do for module positions"
        }
    }

    fun drive(vector: Vector2d, omega: Double) {
        val wheelStates = modulePositions.map { (x, y) ->
            val vxWheel = vector.x() - omega * y
            val vyWheel = vector.y() + omega * x

            val speed = hypot(vxWheel, vyWheel)
            val angle = atan2(vyWheel, vxWheel)

            Pair(speed, angle)
        }

        val maxSpeed = wheelStates.maxOf { it.first }
        val scale = if (maxSpeed > 1.0) 1.0 / maxSpeed else 1.0

        wheelStates.forEachIndexed { i, (speed, angle) ->
            modules[i].setTarget(angle, speed * scale)
        }
    }

    override fun update() {
        modules.forEach { it.update() }
    }
}