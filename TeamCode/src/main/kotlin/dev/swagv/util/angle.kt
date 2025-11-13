package dev.swagv.util

fun Number.d() = Math.toDegrees(this.toDouble())
fun Number.r() = Math.toRadians(this.toDouble())

val Number.normalized: Double
    get() {
        val twoPi = 2.0 * Math.PI
        var rad = this.toDouble() % twoPi
        if (rad < -Math.PI) rad += twoPi
        if (rad > Math.PI) rad -= twoPi
        return rad
    }