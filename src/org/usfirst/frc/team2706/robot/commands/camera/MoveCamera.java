package org.usfirst.frc.team2706.robot.commands.camera;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.subsystems.Camera;

import edu.wpi.first.wpilibj.command.Command;

public class MoveCamera extends Command {
	private float cachedLocationX = Camera.DEFAULT_PAN;
	private float cachedLocationY = Camera.DEFAULT_TILT;
	private Camera.TargetObject target;
	public MoveCamera() {
	     requires(Robot.camera);
	}
	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void execute() {
		try {
			target = Robot.camera.getVisionDataByTarget(Camera.LEFT_TARGET);
			Camera.cachedTarget = target;
			if (target == null) {
				return;
			}
			if(cachedLocationX != target.ctrX && cachedLocationY != target.ctrY) {
			cachedLocationX =  target.ctrX;
			cachedLocationY =  target.ctrY;
			
			SetServoAngles(cachedLocationX,cachedLocationY);
			if(Robot.camera.PRINT_STUFF)
				System.out.println("Network call finished, current location is: " + cachedLocationX + "," + cachedLocationY);
			} 
		}catch(NullPointerException e) {
			if(Robot.camera.PRINT_STUFF)
				System.out.println("Data retrieval failed, resorting to last known values");
			}
	
		}

	
		

	

	@Override
	protected void initialize() {
		System.out.println("Camera Initialized");
		Robot.camera.RawSetServoAxis(Camera.DEFAULT_PAN,Camera.DEFAULT_TILT);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}
	public void SetServoAngles(float panAngle, float tiltAngle) {
		double newPanVal = (panAngle >= 0.0 ? panAngle * panAngle : -1 * panAngle * panAngle) / 5;
		double newTiltVal = (tiltAngle >= 0.0 ? tiltAngle * tiltAngle : -1 * tiltAngle * tiltAngle) / 5;
		Robot.camera.ProtectedSetServoAngles((float)newPanVal,(float)newTiltVal);
	}
}
