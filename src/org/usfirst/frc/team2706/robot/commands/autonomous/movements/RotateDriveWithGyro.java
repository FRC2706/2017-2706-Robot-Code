package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This mostly works, but use the QuickRotate command instead. PID control using gyro heading is
 * slower and needs tuning of P,I,D parameters.
 * 
 * Note that the gyro heading is now absolute and not relative, so angle is a target heading and not
 * a relative turn angle.
 */
public class RotateDriveWithGyro extends Command {

    private final double speed;

    private final double angle;

    private PIDController PID;

    private final int minDoneCycles;

    private final double P = 0.0575, I = 0.02, D = 0.15, F = 0;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param angle The angle to rotate to
     */
    public RotateDriveWithGyro(double speed, double angle, int minDonecycles) {
        requires(Robot.driveTrain);

        this.speed = speed;

        this.angle = angle;

        this.minDoneCycles = minDonecycles;

        PID = new PIDController(P, I, D, F, Robot.driveTrain.getGyroPIDSource(false),
                        Robot.driveTrain.getDrivePIDOutput(false, false, true));
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.reset();



        PID.setInputRange(-360.0, 360.0);

        // Make input infinite
        PID.setContinuous();
        if (speed > 0) {
            // Set output speed range
            PID.setOutputRange(-speed, speed);
        } else {
            PID.setOutputRange(speed, -speed);
        }
        // Will accept within 2 degrees of target
        PID.setAbsoluteTolerance(2);

        PID.setSetpoint(angle);

        // Start going to location
        PID.enable();
    }

    private int doneTicks;

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (PID.onTarget())
            doneTicks++;
        else
            doneTicks = 0;

        return doneTicks >= minDoneCycles;
    }

    // Called once after isFinished returns true
    protected void end() {
        // Disable PID output and stop robot to be safe
        PID.disable();

        Log.d("RotateDriveWithGyro", "Ended");

        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
