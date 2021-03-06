package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Have the robot drive certain distance using the distance sensor(s) on the robot and PID
 */
public class StraightDriveWithDistanceSensor extends Command {

    private final double speed;

    private final double distance;

    private final double error;

    private final PIDController PID;

    private int doneCount;
    private final double P = 7.5, I = 2.5, D = 20;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The ultrasonic distance to travel in inches
     * @param error The range that the robot is happy ending the command in
     */
    public StraightDriveWithDistanceSensor(double speed, double distance, double error) {
        requires(Robot.driveTrain);

        this.speed = speed;

        this.distance = distance;

        this.error = error;

        PID = new PIDController(P, I, D, Robot.driveTrain.getDistanceSensorPIDSource(),
                        Robot.driveTrain.getDrivePIDOutput(true, false, true));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.reset();

        // Make input infinite
        PID.setContinuous();

        // Set output speed range
        if (speed > 0) {
            PID.setOutputRange(-speed, speed);
        } else {
            PID.setOutputRange(speed, -speed);
        }

        Robot.driveTrain.initGyro = Robot.driveTrain.getHeading();

        PID.setSetpoint(distance);


        // Will accept within 5 inch of target
        PID.setAbsoluteTolerance(error);

        // Start going to location
        PID.enable();
    }


    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return PID.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
        doneCount = 0;
        // Disable PID output and stop robot to be safe
        PID.disable();
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

    public int getDoneCount() {
        return doneCount;
    }
}
