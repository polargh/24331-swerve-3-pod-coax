package dev.ftc24331.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.ftc24331.Robot
import dev.nullftc.commandkit.ktx.DSLOpMode
import dev.nullftc.commandkit.ktx.ext.forever
import org.joml.Vector2d
import org.joml.Vector3d

@TeleOp
class Tele: DSLOpMode(false, {
    val robot = Robot(hardwareMap)
    robot.init(telemetry)

    val drive = robot.swerve

    schedule(forever { delta, _ ->
        val movement = Vector2d(gamepad1.left_stick_x.toDouble(), gamepad1.left_stick_y.toDouble())
        val movementRotated = Vector3d(movement.x,0.0,movement.y).rotateY(-robot.getAngle().firstAngle.toDouble())

        drive.drive(Vector2d(movementRotated.x, movementRotated.z), gamepad1.right_stick_x.toDouble())

        robot.update()
        robot.telemetry.addData("fps", 1 / delta)
    })
})