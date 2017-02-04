package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 */
public class StraightDriveWithDistanceSensor extends Command {
    
	private final double speed;
	
	private final double distance;
	
	private final int minDoneCycles;
	
	private final double error;
	
	private final PIDController leftPID;
	private final PIDController rightPID;
	
	private int doneCount;
	private final double P=1.0, I=0.03, D=0.5;
	
	/**
	 * Drive at a specific speed for a certain amount of time
	 * 
	 * @param speed Speed in range [-1,1]
	 * @param distance The ultrasonic distance to travel in inches
	 * @param minDoneCycles The amount of cycles when the robot is within its target range to end the command
	 * @param error The range that the robot is happy ending the command in
	 */
    public StraightDriveWithDistanceSensor(double speed, double distance, int minDoneCycles, double error) {
        requires(Robot.driveTrain);

        this.speed = speed;
        
        this.distance = distance;
        
        this.minDoneCycles = minDoneCycles;
        
        this.error = error;

        leftPID = new PIDController(P,I,D,	 
       		Robot.driveTrain.getDistanceSensorPIDSource(), 
       		Robot.driveTrain.getDrivePIDOutput(false, true)
        );
        
        rightPID = new PIDController(P,I,D,	 
           	Robot.driveTrain.getDistanceSensorPIDSource(), 
           	Robot.driveTrain.getDrivePIDOutput(false, false)
        );
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.reset();
    	
    	// Make input infinite
    	leftPID.setContinuous();
    	rightPID.setContinuous();
    	
    	// Set output speed range
    	if(speed > 0) {
    		leftPID.setOutputRange(-speed, speed);
    		rightPID.setOutputRange(-speed, speed);
    	}
    	else {
    		leftPID.setOutputRange(speed, -speed);
        	rightPID.setOutputRange(speed, -speed);
    	}

		Robot.driveTrain.initGyro = Robot.driveTrain.getHeading();
		
    	leftPID.setSetpoint(distance);
    	rightPID.setSetpoint(distance);
    	
    	
    	// Will accept within 5 inch of target
    	leftPID.setAbsoluteTolerance(error);
    	rightPID.setAbsoluteTolerance(error);
    	
    	// Start going to location
    	leftPID.enable();
    	//rightPID.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {    	
    	// TODO: Use WPI onTarget()
    	onTarget();
    }
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {  
    	if(this.doneCount > this.minDoneCycles) {
    		System.out.println("Command Ended!");
    	
    		return true;
    	
    	}
    	else {
    		return false;
    	}

    }

    // Called once after isFinished returns true
    protected void end() {
    	doneCount = 0;
    	// Disable PID output and stop robot to be safe
    	leftPID.disable();
    	rightPID.disable();
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
    
    private boolean onTarget() {
    	if(Math.abs(leftPID.getError()) < error && Math.abs(rightPID.getError()) < error) {
    		doneCount++;
    		return true;
    	}
    	else {
    		doneCount = 0;
    		return false;
    	}
    		
    }
    
    public int getDoneCount() {
    	return doneCount;
    }
}

