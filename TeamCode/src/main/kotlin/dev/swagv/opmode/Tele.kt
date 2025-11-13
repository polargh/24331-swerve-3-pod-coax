package dev.swagv.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.swagv.Robot
import dev.nullftc.commandkit.ktx.DSLOpMode
import dev.nullftc.commandkit.ktx.ext.forever
import dev.swagv.util.r
import org.joml.Vector2d
import org.joml.Vector3d

@TeleOp
class Tele: DSLOpMode(false, {
    val robot = Robot(hardwareMap)
    robot.init(telemetry)

    val drive = robot.swerve

    schedule(forever { delta, _ ->
        val movement = Vector2d(gamepad1.left_stick_x.toDouble(), gamepad1.left_stick_y.toDouble())

        val movementRotated = Vector3d(movement.x, 0.0, movement.y).rotateY(30.r())

        drive.drive(movement, gamepad1.right_stick_x.toDouble())

        robot.update()
        robot.telemetry.addData("fps", 1 / delta)
        robot.telemetry.addData("left", robot.left.turnEncoder.currentPosition)
        robot.telemetry.addData("right", robot.right.turnEncoder.currentPosition)
        robot.telemetry.addData("back", robot.back.turnEncoder.currentPosition)
        robot.telemetry.update()
    })
})