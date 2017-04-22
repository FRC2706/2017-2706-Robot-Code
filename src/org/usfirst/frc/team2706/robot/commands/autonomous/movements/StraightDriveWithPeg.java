package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Have the robot drive certain distance
 */
public class StraightDriveWithPeg extends Command {

    private final double speed;

    private final double distance;

    private final double error;

    private PIDController PID;

    private final int minDoneCycles;

    private final double P = 7.5, I = 2.0, D = 25, F = 0;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param distance The encoder distance to travel
     * @param error The range that the robot is happy ending the command in
     */
    public StraightDriveWithPeg(double speed, double distance, double error, int minDoneCycles) {
        requires(Robot.driveTrain);

        this.speed = speed;

        this.distance = distance;

        this.error = error / 12.0;

        this.minDoneCycles = minDoneCycles;

        PID = new PIDController(P, I, D, F, Robot.driveTrain.getAverageEncoderPIDSource(),
                        Robot.driveTrain.getDrivePIDOutput(true, false, false));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.reset();

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

        // Start going to location
        PID.enable();
    }

    private int doneTicks;

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (Robot.gearHandler.pegDetected())
            doneTicks++;

        return doneTicks >= minDoneCycles;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Robot.driveTrain.brakeMode(false);
        // Disable PID output and stop robot to be safe
        PID.disable();
        Robot.driveTrain.drive(0, 0);

        Log.d("StraightDriveWithPeg", "Ended");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
