package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TakeOverCommandBreak extends Command {
    public TakeOverCommandBreak() {
        requires(Robot.driveTrain);
    }
    protected void initialize() {
        Robot.driveTrain.brakeMode(true);
    }
    protected void end() {
        Robot.driveTrain.brakeMode(false);
    }
    protected void interrupted() {
        end();
    }
    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }
}
