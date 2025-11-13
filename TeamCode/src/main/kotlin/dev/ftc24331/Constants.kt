package dev.ftc24331

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.PIDCoefficients

@Config
object Constants {
    const val CENTER_DIST: Double = 0.143

    @JvmStatic var coefficients: PIDCoefficients = PIDCoefficients(0.0, 0.0, 0.0)
        set(value) {
            for (module in Robot.instance.swerve.modules) {
                module.turnCoefficients = value
            }
            field = value
        }

    @JvmStatic var leftEncoderOffset: Double = 0.0
    @JvmStatic var rightEncoderOffset: Double = 0.0
    @JvmStatic var backEncoderOffset: Double = 0.0
}