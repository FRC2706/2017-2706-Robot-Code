package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Have the robot drive certain distance
 */
public class QuickStraightDriveWithDistanceSensor extends Command {
    
	private final double speed;
	
	private final double distance1;
	private final double distance2;
	
	private final double alpha;
	
	private boolean done;
	private boolean forward;
	
	private int desiredStopCycles;
	
	private int stopCycles;
	
	private double sensorAverage;
	
	/**
	 * Drive at a specific speed for a certain amount of time
	 * 
	 * @param speed Speed in range [-1,1]
	 * @param distance1 The min ultrasonic distance to travel in inches
	 * @param distance2 The max ultrasonic distance to travel in inches
	 * @param desiredStopCylces The amount of cycles the robot applies the reverse jolt or brakes
	 * @param alpha The amount of weight given to the current value compared to the overall average (between 0 and 1)
	 */
    public QuickStraightDriveWithDistanceSensor(double speed, double distance1, double distance2, int desiredStopCycles, double alpha) {
        requires(Robot.driveTrain);

        this.speed = speed;
        
        this.distance1 = distance1;
        this.distance2 = distance2;
        
        this.desiredStopCycles = desiredStopCycles;
        
        this.alpha = alpha;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.reset();

		Robot.driveTrain.initGyro = Robot.driveTrain.getHeading();
		
		done = false;
		stopCycles = 0;
		
//    	SmartDashboard.putNumber("Smoothed Distance", 0);
//    	SmartDashboard.putNumber("Distance Sensor", 0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {  
    	sensorAverage = alpha * Robot.driveTrain.getDistanceToObstacle() + (1 - alpha) * sensorAverage;
    	
//    	SmartDashboard.putNumber("Smoothed Distance", sensorAverage);
//    	SmartDashboard.putNumber("Distance Sensor", Robot.driveTrain.getDistanceToObstacle());
    	
    	if(sensorAverage > distance1 && sensorAverage < distance2) {
    		// TODO: Use CANTalons to do an actual break
    		//		 instead of a jolt in the opposite direction
    		if(stopCycles < desiredStopCycles) {
    			double rotateVal = (Robot.driveTrain.normalize(Robot.driveTrain.getHeading() - Robot.driveTrain.initGyro) * 0.1);
				Robot.driveTrain.arcadeDrive(forward ? speed : -speed, -rotateVal);
				stopCycles++;
    		}
    		else
    			done = true;
    	}
    	else if(sensorAverage < distance1) {
    		forward = true;
    		double rotateVal = (Robot.driveTrain.normalize(Robot.driveTrain.getHeading() - Robot.driveTrain.initGyro) * 0.1);
			Robot.driveTrain.arcadeDrive(-speed, -rotateVal);
    	}
    	else if(sensorAverage > distance2) {
    		forward = false;
    		double rotateVal = (Robot.driveTrain.normalize(Robot.driveTrain.getHeading() - Robot.driveTrain.initGyro) * 0.1);
			Robot.driveTrain.arcadeDrive(speed, -rotateVal);
    	}
    }
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {  
    		return done;

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

