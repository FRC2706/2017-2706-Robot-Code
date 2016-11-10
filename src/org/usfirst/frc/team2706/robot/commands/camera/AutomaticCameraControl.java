package org.usfirst.frc.team2706.robot.commands.camera;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutomaticCameraControl extends Command {
	
	private MoveCamera move;
	private SearchForTarget search;
	
	// System variables
	private boolean lostTarget = false;
	@Override
	protected void end() {
		
	}
	@Override
	protected void execute() {
		SmartDashboard.putNumber("Vision Angle", Robot.camera.RobotTurnDegrees());
		if(Robot.camera.getVisionData() != null) {
			lostTarget = false;
			move.start();
			search.end();
		}
		else {
			
			if(lostTarget == false) {
				lostTarget = true;
			}
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
