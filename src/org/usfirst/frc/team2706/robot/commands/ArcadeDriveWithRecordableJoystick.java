package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

public class ArcadeDriveWithRecordableJoystick extends ArcadeDriveWithJoystick {
	
	public static final String JOY_LOC = "/home/lvuser/joystick";
	
	public ArcadeDriveWithRecordableJoystick() {
		super(new RecordableJoystick(Robot.oi.getDriverJoystick(), JOY_LOC));
	}
	
	@Override
	public void execute() {
		super.execute();
		
		if(!((RecordableJoystick)joystick).update())
			this.cancel();
	}
}
