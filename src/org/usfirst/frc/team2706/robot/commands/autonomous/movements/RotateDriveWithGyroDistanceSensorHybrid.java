package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

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
public class RotateDriveWithGyroDistanceSensorHybrid extends Command {

    private final double speed;

    private double angle;

    private final double P = 1, I = 0.03, D = 0.5, F = 0;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param angle The angle to rotate to
     */
    public RotateDriveWithGyroDistanceSensorHybrid(double speed) {
        requires(Robot.driveTrain);

        this.speed = speed;
    }
    QuickRotate q;
    // Called just before this Command runs the first time
    protected void initialize() {
        q = new QuickRotate(Robot.driveTrain.GetAngleWithDistanceSensors());
    }

    protected void execute() {
        if(!q.isRunning()) {
            System.out.println("started");
            q.start();
        }
    }
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
       return !q.isRunning();
    }

    // Called once after isFinished returns true
    protected void end() {

        Robot.driveTrain.drive(0, 0);
    }

    /**
     * Called when another command which requires one or more of the same subsystems is scheduled to run
     */
    protected void interrupted() {
        end();
    }
}
