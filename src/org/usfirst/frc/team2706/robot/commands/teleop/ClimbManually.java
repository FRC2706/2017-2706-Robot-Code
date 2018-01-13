package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command is activated when the driver holds the corresponding button, turning the motor in
 * the climber mechanism.
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class ClimbManually extends Command {

    public void initialize() {
        // Turn compressor off for climb.
        Robot.gearHandler.setCompressor(false);
    }

    protected void execute() {
        Robot.climber.climb();
    }

    @Override
    protected boolean isFinished() {
        // finished is always false because we are using EJoystickButton.runWhileHeld
        // which will call end() when the button is released
        return false;
    }

    protected void end() {
        Robot.climber.stop();
        // Turn compressor back on.
        Robot.gearHandler.setCompressor(true);
    }
}
