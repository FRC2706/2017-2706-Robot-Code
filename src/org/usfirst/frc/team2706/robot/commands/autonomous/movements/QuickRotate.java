package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A very simple two-stage proportional control for quickly rotating the robot heading based on the
 * robot gyro.
 */
public class QuickRotate extends Command {

    // Gyro headings - what we want to be at, where we are now
    private double targetHeading;
    private double currentHeading;

    // Clockwise or counterclockwise rotation
    private int direction = 1;

    // Rotate faster if far away from target heading
    private double fastRotateSpeed = 0.75;

    // Rotate slower when approaching target heading
    private double slowRotateSpeed = 0.6;

    // Threshold (degrees) at which to switch from fast to slow
    private double speedThreshold = 25.0;

    private int maxCycles = 120;

    // "Close enough"
    private double arrivalThreshold = 2.0;

    private int done = 10;

    /**
     * Rotates by the specified angle
     * 
     * @param targetHeading The angle the robot will rotate to (-360 to 360)
     */
    public QuickRotate(double targetHeading) {
        super("QuickRotate");

        requires(Robot.driveTrain);

        this.targetHeading = normalize(targetHeading);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        done = 10;
        maxCycles = 120;

        Robot.driveTrain.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        System.out.println(Robot.driveTrain.getHeading());
        currentHeading = normalize(Robot.driveTrain.getHeading());
        double error = normalize(targetHeading - currentHeading);

        // which direction are we off by?
        direction = (error > 0.0 ? -1 : 1);

        if (Math.abs(error) <= arrivalThreshold) {
            // close enough! We're done
            done--;
            Robot.driveTrain.drive(0, 0);
        } else if (Math.abs(error) > speedThreshold) {
            // turn fast
            Robot.driveTrain.drive(direction * fastRotateSpeed, -direction * fastRotateSpeed);
        } else {
            // turn slow
            Robot.driveTrain.drive(direction * slowRotateSpeed, -direction * slowRotateSpeed);
        }
        maxCycles--;
    }

    private double normalize(double input) {
        double normalizedValue = input;
        while (normalizedValue > 180)
            normalizedValue -= 360;
        while (normalizedValue < -180)
            normalizedValue += 360;

        return normalizedValue;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (done <= 0 || (maxCycles <= 0))
            return true;
        else
            return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        done = 0;
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
