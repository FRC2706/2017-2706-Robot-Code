package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command is activated when the driver holds the corresponding button, turning the motor in
 * the climber mechanism.
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class ClimbAutomatically extends Command {

    boolean finished = false;

    protected void initialize() {
        Robot.climber.startVerifyingClimb();
        Robot.climber.climb();
    }

    protected void execute() {
        if ((Robot.climber.isClimbing()) && (Robot.climber.isHittingTouchpad())) {
            stopClimbing();
        }
    }

    protected void interrupted() {
        stopClimbing();
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }

    protected void end() {
        stopClimbing();
    }

    private void stopClimbing() {
        Robot.climber.stop();
        finished = true;
    }
}
