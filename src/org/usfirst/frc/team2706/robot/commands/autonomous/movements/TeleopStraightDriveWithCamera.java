package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import java.util.TimerTask;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot in a straight line towards the target found by the camera. Used for lining up
 * the peg at short distances
 */
public class TeleopStraightDriveWithCamera extends Command {

    /**
     * Drive at a specific speed based on camera
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in inches
     */
    public TeleopStraightDriveWithCamera() {
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    protected void execute() {
        Robot.camera.GetTargets();
        double rotateVal;
        if (Robot.camera.getTarget() != null) {
            if (Robot.camera.getTarget().ctrX > -0.8 && Robot.camera.getTarget().ctrX < 0.8) {
                rotateVal = Robot.camera.getTarget() != null ? (Robot.camera.getTarget().ctrY) * 2.5
                                : 0;
                if (rotateVal > 0.5) {
                    rotateVal = 0.5;
                }
                if (rotateVal < -0.5) {
                    rotateVal = -0.5;
                }
            } else {
                rotateVal = 0;
            }
        } else {
            rotateVal = 0;
        }
        if (Robot.driveTrain.getDistanceToObstacle() < 25 || Robot.camera.getTarget() == null) {
            rotateVal = 0;
        }
        System.out.println(rotateVal);
        Robot.driveTrain.arcadeDrive(-getTriggerValue(), rotateVal);
    }

    private double getTriggerValue() {
        return Robot.oi.getDriverJoystick().getRawAxis(3);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return getTriggerValue() <= 0;
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

    class CommandTimerTask extends TimerTask {

        public void run() {
            done = true;
        }
    }
}
