package org.usfirst.frc.team2706.robot.commands.autonomous.movements;


import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Have the robot drive certain amount of time
 */
public class RotateDriveWithCamera extends Command {
    
	private final double speed;
	
	private final int minDoneCycles;
	
	private final PIDController leftPID;
	private final PIDController rightPID;
	
	private int doneCount;
	
	private final double P=0.25, I=0.03125, D=0.03125, F=0;
	
	/**
	 * Turn to the target
	 * 
	 * @param speed Speed in range [-1,1]
	 */
    public RotateDriveWithCamera(double speed, int minDoneCycles) {
        requires(Robot.driveTrain);
        
        this.speed = speed;

        this.minDoneCycles = minDoneCycles;
        leftPID = new PIDController(P, I, D, F, Robot.camera.getCameraPIDSource(true), 
        		Robot.driveTrain.getDrivePIDOutput(false, true));
        
        rightPID = new PIDController(P, I, D, F, Robot.camera.getCameraPIDSource(true), 
        		Robot.driveTrain.getDrivePIDOutput(true, false));
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    	Robot.driveTrain.reset();
    	
    	// Use gyro if there is no target
/*    	if(!Robot.camera.HasTarget()) {
    		new RotateDriveWithGyro(0.85, 45, 100).start();
    		this.cancel();
    		return;
    	}*/
    	
    	leftPID.setInputRange(-90.0, 90.0);
    	rightPID.setInputRange(-90.0, 90.0);
    	
    	// Make input infinite
    	leftPID.setContinuous();
    	rightPID.setContinuous();
    	
    	// Set output speed range
    	leftPID.setOutputRange(-speed, speed);
    	rightPID.setOutputRange(-speed, speed);
    	
    	// Will accept within 5 degrees of target
    	leftPID.setAbsoluteTolerance(5);
    	rightPID.setAbsoluteTolerance(5);
    	
    	// Have camera centered (on target)
    	leftPID.setSetpoint(0);
    	rightPID.setSetpoint(0);
    	
    	// Start going to location
    	leftPID.enable();
    	rightPID.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// TODO: Use WPI onTarget()
    	onTarget();
    	
    	System.out.println("Tick!!!");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(this.doneCount > this.minDoneCycles)
    		return true;
    	else
    		return false;
    	
    }

    // Called once after isFinished returns true
    protected void end() {
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
    	if(leftPID.getError() < 5.0 && rightPID.getError() < 5.0) {
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
