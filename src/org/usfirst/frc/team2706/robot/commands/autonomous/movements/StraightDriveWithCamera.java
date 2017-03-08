package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drives the robot in a straight line towards the target found by the camera.
 * Used for lining up the peg at short distances
 */
public class StraightDriveWithCamera extends Command {

    private double speed;

    private final double distance;

    private final double error;

    private final PIDController PID;

    private final double P = 0.5, I = 0.06, D = 0.25;

    /**
     * Drive at a specific speed based on camera
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in inches
     */
    public StraightDriveWithCamera(double speed, double distance, double error) {
        requires(Robot.driveTrain);

        this.speed = speed;

        this.distance = distance;

        this.error = error / 12.0;

        PID = new PIDController(P, I, D, Robot.driveTrain.getDistanceSensorPIDSource(),
                        Robot.driveTrain.getDrivePIDOutput(true, true, true));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.resetEncoders();
        
        Robot.driveTrain.brakeMode(true);

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
        System.out.println("init");
        // Start going to location
        PID.enable();
    }
    
    protected void execute() {
        Robot.camera.GetTargets();
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return PID.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.brakeMode(false);

        // Disable PID output and stop robot to be safe
        PID.disable();
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
