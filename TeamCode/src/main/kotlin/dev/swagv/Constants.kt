package dev.swagv

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.PIDCoefficients

@Config
object Constants {
    const val CENTER_DIST: Double = 0.143

    @JvmField var pidP: Double = 0.0
    @JvmField var pidI: Double = 0.0
    @JvmField var pidD: Double = 0.0

    @JvmField var leftEncoderOffset: Double = 0.0
    @JvmField var rightEncoderOffset: Double = 0.0
    @JvmField var backEncoderOffset: Double = 0.0

    fun applyPIDToModules() {
        val coeffs = PIDCoefficients(pidP, pidI, pidD)
        Robot.instance.swerve.modules.forEach { it.turnCoefficients = coeffs }
    }
}
