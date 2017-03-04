package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Keeps robot in brake mode until the command is interrupted
 */
public class HandBrake extends Command {

    /**
     * Puts robot into brake mode and can disable driving if wanted
     * 
     * @param stopDriving Whether to disable driving
     */
    public HandBrake(boolean stopDriving) {
        if (stopDriving)
            requires(Robot.driveTrain);
    }

    @Override
    protected void initialize() {
        Robot.driveTrain.brakeMode(true);
    }

    @Override
    protected void end() {
        System.out.println("ended");
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
