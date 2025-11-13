package dev.swagv.subsystem

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.PIDCoefficients
import dev.swagv.control.PIDController
import dev.swagv.hardware.AbsoluteAnalogEncoder
import dev.swagv.util.Periodic
import dev.swagv.util.normalized

class SwerveModule(
    val driveMotor: DcMotorEx,
    val turnServo: CRServo,
    val turnEncoder: AbsoluteAnalogEncoder,
    private val turnGearRatio: Double = 1.0
) : Periodic {

    private val turnPID = PIDController(0.015, 0.0, 0.0005).apply {
        continuous = true
        minInput = -Math.PI
        maxInput = Math.PI
        outputRange = -1.0..1.0
    }

    private var targetAngle = 0.0
    private var drivePower = 0.0

    var turnCoefficients: PIDCoefficients
        get() = PIDCoefficients(turnPID.kP, turnPID.kI, turnPID.kD)
        set(value) {
            turnPID.kP = value.p
            turnPID.kI = value.i
            turnPID.kD = value.d
        }

    fun setTarget(angleRadians: Double, power: Double) {
        targetAngle = angleRadians
        drivePower = power
    }

    override fun update() {
//        val currentAngle = (turnEncoder.currentPosition / turnGearRatio).normalized
//
//        val target = (targetAngle * turnGearRatio).normalized
//
//        val turnOutput = turnPID.calculate(currentAngle, target)
//        turnServo.power = turnOutput.coerceIn(-1.0, 1.0)
//        driveMotor.power = drivePower
    }
}
