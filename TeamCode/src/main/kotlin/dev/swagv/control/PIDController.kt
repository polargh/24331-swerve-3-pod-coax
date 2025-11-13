package dev.swagv.control

class PIDController(
    var kP: Double,
    var kI: Double,
    var kD: Double,
    var setpoint: Double = 0.0,
) {
    private var lastError = 0.0
    private var integral = 0.0
    private var lastTimestamp = System.nanoTime() / 1e9

    var integralLimit = Double.POSITIVE_INFINITY
    var outputRange: ClosedFloatingPointRange<Double>? = null

    var continuous = false
    var minInput = -Math.PI
    var maxInput = Math.PI

    fun reset() {
        lastError = 0.0
        integral = 0.0
        lastTimestamp = System.nanoTime() / 1e9
    }

    fun calculate(measurement: Double): Double {
        return calculate(measurement, setpoint)
    }

    fun calculate(measurement: Double, setpoint: Double): Double {
        val now = System.nanoTime() / 1e9
        val dt = (now - lastTimestamp).coerceAtLeast(1e-6)
        lastTimestamp = now

        var error = setpoint - measurement

        if (continuous) {
            val range = maxInput - minInput
            error = ((error + range / 2) % range + range) % range - range / 2
        }

        integral += error * dt
        integral = integral.coerceIn(-integralLimit, integralLimit)

        val derivative = (error - lastError) / dt
        lastError = error

        var output = (kP * error) + (kI * integral) + (kD * derivative)
        outputRange?.let { output = output.coerceIn(it.start, it.endInclusive) }

        return output
    }
}
