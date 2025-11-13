package dev.swagv.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.swagv.Robot
import dev.nullftc.commandkit.ktx.DSLOpMode
import dev.nullftc.commandkit.ktx.ext.forever
import org.joml.Vector2d

@TeleOp
class Tele: DSLOpMode(false, {
    val robot = Robot(hardwareMap)
    robot.init(telemetry)

    val drive = robot.swerve

    schedule(forever { delta, _ ->
        val movement = Vector2d(gamepad1.left_stick_x.toDouble(), gamepad1.left_stick_y.toDouble())

        for (module in drive.modules) {
            module.turnServo.power = 1.0
        }

        robot.update()
        robot.telemetry.addData("fps", 1 / delta)
        robot.telemetry.update()
    })
})