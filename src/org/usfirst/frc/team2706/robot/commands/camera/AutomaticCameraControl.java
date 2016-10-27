package org.usfirst.frc.team2706.robot.commands.camera;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutomaticCameraControl extends Command {
	public int waitTime = 5;
	MoveCamera move;
	SearchForTarget search;
	//System variables
	long savedMilis;
	boolean lostTarget = false;
	@Override
	protected void end() {
		
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Vision Angle", Robot.camera.RobotTurnDegrees());
		if(Robot.camera.getVisionData() != null) {
			if(lostTarget == true) {
			//Robot.oi.getJoystick().setRumble(RumbleType.kLeftRumble, 0.5f);
			}
			lostTarget = false;
			move.start();
			search.end();
		}
		else {
			
			if(lostTarget == false) {
				//Robot.oi.getJoystick().setRumble(RumbleType.kRightRumble, 0.5f);
				savedMilis = System.currentTimeMillis();
				lostTarget = true;
			}
			/*if(System.currentTimeMillis() - (waitTime * 1000) > savedMilis) {

				move.end();
				if(search.isRunning()) {
				search.end();
				}
				search.start();
			}*/
		}

	}

	@Override
	protected void initialize() {
		move = new MoveCamera();
		search = new SearchForTarget();
		move.start();
		System.out.println("Automatic Camera Control Started");
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
