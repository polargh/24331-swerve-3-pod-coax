package dev.swagv

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.configuration.LynxConstants
import dev.swagv.hardware.AbsoluteAnalogEncoder
import dev.swagv.subsystem.SwerveDrive
import dev.swagv.subsystem.SwerveModule
import dev.swagv.util.Periodic
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.joml.Vector2d
import kotlin.math.sqrt

class Robot(private val hardware: HardwareMap) : Periodic {

    // Drive
    lateinit var swerve: SwerveDrive

    // Telemetry
    lateinit var telemetry: Telemetry

    // Hubs
    private lateinit var allHubs: List<LynxModule>
    private lateinit var controlHub: LynxModule
    private lateinit var imu: IMU

    enum class ModuleLocation(val serialized: String) {
        FRONT_LEFT("frontLeft"),
        FRONT_RIGHT("frontRight"),
        BACK("back")
    }

    fun init(telemetry: Telemetry) {
        instance = this
        this.telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        allHubs = hardware.getAll(LynxModule::class.java)
        allHubs.forEach { hub ->
            hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
            if (hub.isParent && LynxConstants.isEmbeddedSerialNumber(hub.serialNumber)) {
                controlHub = hub
            }
        }

        imu = hardware.get(IMU::class.java, "imu")

        val frontLeftPod: SwerveModule = getModule(ModuleLocation.FRONT_LEFT)
        val frontRightPod: SwerveModule = getModule(ModuleLocation.FRONT_RIGHT)
        val backPod: SwerveModule = getModule(ModuleLocation.BACK)

        swerve = SwerveDrive(
            listOf(frontLeftPod, frontRightPod, backPod),
            listOf(
                0.0 to Constants.CENTER_DIST,
                -sqrt(3.0) / 2 * Constants.CENTER_DIST to -0.5 * Constants.CENTER_DIST,
                sqrt(3.0) / 2 * Constants.CENTER_DIST to -0.5 * Constants.CENTER_DIST
            )
        )

        frontLeftPod.turnEncoder.zero(Constants.leftEncoderOffset)
        frontRightPod.turnEncoder.zero(Constants.leftEncoderOffset)
        backPod.turnEncoder.zero(Constants.leftEncoderOffset)
    }

    fun getAngle(): Orientation {
        return imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS)
    }

    fun drive(vector: Vector2d, angle: Double) {
        swerve.drive(vector, angle)
    }

    override fun update() {
        swerve.update()
        Constants.applyPIDToModules()
    }

    private fun getModule(location: ModuleLocation): SwerveModule {
        val encoder = hardware.get(AnalogInput::class.java, "${location.serialized}Encoder")
        val motor = hardware.get(DcMotorEx::class.java, "${location.serialized}Motor")
        val servo = hardware.get(CRServo::class.java, "${location.serialized}Servo")

        return SwerveModule(motor, servo, AbsoluteAnalogEncoder(encoder), (32.0 / 34.0))
    }

    companion object {
        lateinit var instance: Robot
    }
}
