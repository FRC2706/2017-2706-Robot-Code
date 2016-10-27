package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain.DrivePIDOutput;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Have the robot drive certain amount of time
 */
public class RotateDriveWithHybrid extends Command {
    
	private final double speed;
	
	private final int minDoneCycles;
	
	private final PIDController leftPID;
	private final PIDController rightPID;
	
	private int doneCount;
	
	private final double P=0.25, I=0.03125, D=0.03125, F=0;
	
	private final DrivePIDOutput leftOut = (DrivePIDOutput) Robot.driveTrain.getDrivePIDOutput(false, true);
	private final DrivePIDOutput rightOut = (DrivePIDOutput) Robot.driveTrain.getDrivePIDOutput(true, false);
	
	/**
	 * Drive at a specific speed for a certain amount of time
	 * 
	 * @param speed Speed in range [-1,1]
	 * @param angle The angle to rotate to
	 */
    public RotateDriveWithHybrid(double speed, int minDoneCycles) {
        requires(Robot.driveTrain);

        this.speed = speed;

        this.minDoneCycles = minDoneCycles;
        leftPID = new PIDController(P, I, D, F, Robot.driveTrain.getGyroPIDSource(false), 
        		leftOut);
        
        rightPID = new PIDController(P, I, D, F, Robot.driveTrain.getGyroPIDSource(false), 
        		rightOut);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveTrain.reset();
    	
    	doneCount = 0;
    	
    	leftPID.setInputRange(0.0, 360.0);
    	rightPID.setInputRange(0.0, 360.0);
    	
    	// Make input infinite
    	leftPID.setContinuous();
    	rightPID.setContinuous();
    	
    	// Set output speed range
    	leftPID.setOutputRange(-speed, speed);
    	rightPID.setOutputRange(-speed, speed);
    	
    	// Will accept within 1 degrees of target
    	leftPID.setAbsoluteTolerance(4);
    	rightPID.setAbsoluteTolerance(4);
    	
//    	if(Robot.camera.RobotTurnDegrees() < 0) {
//    		leftOut.setInvert(true);
//    		rightOut.setInvert(false);
//    		Robot.driveTrain.inverGyroPIDSource(true);
//    	}
//    	else {
//    		leftOut.setInvert(false);
//    		rightOut.setInvert(true);
//    		Robot.driveTrain.inverGyroPIDSource(false);
//    	}
    	
    	leftPID.setSetpoint(Robot.camera.RobotTurnDegrees());
    	rightPID.setSetpoint(Robot.camera.RobotTurnDegrees());
    	
    	// Start going to location
    	leftPID.enable();
    	rightPID.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// TODO: Use WPI onTarget()
    	onTarget();
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
    	if(leftPID.getError() < 4.0 && rightPID.getError() < 4.0) {
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
