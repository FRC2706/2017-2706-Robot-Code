package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot in a straight line towards the target found by the camera. Used for lining up
 * the peg at short distances
 */
public class AlignWithCamera extends Command {

    @SuppressWarnings("unused")
    private double speed;

    private final long time;

    @SuppressWarnings("unused")
    private final double error;

    /**
     * Drive at a specific speed based on camera
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in inches
     */
    public AlignWithCamera(double speed, long time, double error) {
        requires(Robot.driveTrain);

        this.speed = speed;

        this.time = time;

        this.error = error / 12.0;

    }

    // Called just before this Command runs the first time
    protected void initialize() {

        CommandTimerTask interrupt = new CommandTimerTask();
        new Timer().schedule(interrupt, time);

        done = false;
    }

    protected void execute() {
        Robot.camera.GetTargets(true);
        double rotateVal;
        if (Robot.camera.getTarget() != null) {
            if (Robot.camera.getTarget().ctrX > -0.8 && Robot.camera.getTarget().ctrX < 0.8) {
                rotateVal = Robot.camera.getTarget() != null ? Robot.camera.getTarget().ctrY * 0.8
                                : 0;
            } else {
                rotateVal = 0;
            }
        } else {
            rotateVal = 0;
        }
        Robot.driveTrain.arcadeDrive(0.0, rotateVal);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Robot.camera.enableRingLight(false);
        Robot.driveTrain.brakeMode(false);

        // Disable PID output and stop robot to be safe
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }


    public boolean done;

    private class CommandTimerTask extends TimerTask {

        public void run() {
            done = true;
        }
    }
}
