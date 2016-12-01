package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

public class ArcadeDriveWithRecordableJoystick extends ArcadeDriveWithJoystick {
	
	public ArcadeDriveWithRecordableJoystick(String loc) {
		super(new RecordableJoystick(Robot.oi.getDriverJoystick(), loc));
	}
	
	@Override
	public void execute() {
		super.execute();
		
		if(!((RecordableJoystick)joystick).update())
			this.cancel();
	}
}
