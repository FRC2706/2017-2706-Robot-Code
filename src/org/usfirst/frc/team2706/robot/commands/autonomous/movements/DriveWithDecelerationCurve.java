package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveWithDecelerationCurve extends Command {

    private final double desiredDistanceToObstacle;
    private final double maximumDistanceFromObstacle;

    private final double minimumSpeed;
    private final double maximumSpeed;
    
    private final boolean invert;

    private double a;
    private double stopFromTargetDistance;

    private final double alpha;

    private double sensorAverage;

    /**
     * Drives the robot in a straight line and deccelerates as the robot gets closer to the target
     * 
     * @param desiredDistanceToObstacle The desired distance for the robot to be from the obstacle
     * @param maximumDistanceFromObstacle The range where the robot stops when the distance to
     *        target is within
     * @param minimumSpeed The minimum speed that the robot will drive at
     * @param maximumSpeed The fastest the robot will drive at
     * @param alpha The weight given to the current sensor data, verses the previous weighted
     *        average
     * @param invert Whether to invert the output given to the motors
     */
    public DriveWithDecelerationCurve(double desiredDistanceToObstacle,
                    double maximumDistanceFromObstacle, double minimumSpeed, double maximumSpeed,
                    double alpha, boolean invert) {
        requires(Robot.driveTrain);

        this.desiredDistanceToObstacle = desiredDistanceToObstacle;
        this.maximumDistanceFromObstacle = maximumDistanceFromObstacle;
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.alpha = alpha;
        this.invert = invert;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        sensorAverage = Robot.driveTrain.getDistanceToObstacle();

        double initialDistanceToObstacle = Math.abs(desiredDistanceToObstacle - sensorAverage);

        // Simulates brake mode, but not very well, should be removed later
        stopFromTargetDistance = initialDistanceToObstacle * 0.2;
        a = (maximumSpeed - minimumSpeed)
                        / (1 - (1 / (1 + (initialDistanceToObstacle - stopFromTargetDistance))));

        Robot.driveTrain.reset();
        Robot.driveTrain.initGyro = 0.0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        sensorAverage = alpha * Robot.driveTrain.getDistanceToObstacle()
                        + (1 - alpha) * sensorAverage;

        double distanceToObstacle = Math
                        .abs(desiredDistanceToObstacle - Robot.driveTrain.getDistanceToObstacle());
        double driveSpeed = a * (1 - (1 / (1 + (distanceToObstacle - stopFromTargetDistance))))
                        + minimumSpeed;

        // Make sure to stop driving once the stopFromTargetDistance is reached
        if (distanceToObstacle < stopFromTargetDistance)
            driveSpeed = minimumSpeed;

        // Drive backwards in same pattern if target is behind robot
        if ((desiredDistanceToObstacle - Robot.driveTrain.getDistanceToObstacle()) < 0)
            driveSpeed = -driveSpeed;

        // Invert output in case the ultrasonics are on the front
        if(invert)
            driveSpeed = -driveSpeed;
        
        double rotateVal = (Robot.driveTrain.normalize(
                        Robot.driveTrain.getHeading() - Robot.driveTrain.initGyro) * 0.1);
        Robot.driveTrain.arcadeDrive(-driveSpeed, -rotateVal);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(desiredDistanceToObstacle
                        - Robot.driveTrain.getDistanceToObstacle()) < maximumDistanceFromObstacle;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
