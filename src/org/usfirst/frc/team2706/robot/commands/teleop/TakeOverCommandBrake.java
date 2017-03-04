package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Until interrupted, stops robot from driving around, and goes into brake mode to stop the robot
 */
public class TakeOverCommandBrake extends Command {

    public TakeOverCommandBrake() {
        requires(Robot.driveTrain);
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.brakeMode(true);
    }

    @Override
    protected void end() {
        Robot.driveTrain.brakeMode(false);
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
