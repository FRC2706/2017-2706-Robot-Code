package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command is activated when the driver holds the corresponding trigger, turning the motor faster when more pressure is applied
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class ClimbVariableManually extends Command {

    protected void execute() {
        Robot.climber.setClimberSpeed(getTriggerValue());
    }

    @Override
    // Determines if the trigger is being pressed, and if not then the motor does not rotate
    protected boolean isFinished() {
        if (getTriggerValue() <= 0) {
            return true;
        }
        return false;
    }

    protected void end() {
        Robot.climber.stop();
    }
    
    private double getTriggerValue() {
        return Robot.oi.getOperatorJoystick().getRawAxis(3);
    }
}
