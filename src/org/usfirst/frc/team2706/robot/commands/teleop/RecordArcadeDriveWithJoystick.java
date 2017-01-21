package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

public class RecordArcadeDriveWithJoystick extends ArcadeDriveWithJoystick {
	
	public RecordArcadeDriveWithJoystick(String loc) {
		super(new RecordableJoystick(Robot.oi.getDriverJoystick(), loc, false));
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		((RecordableJoystick)joystick).init(this::timeSinceInitialized);
	}
	
	@Override
	public void execute() {
		super.execute();
		
		if(!((RecordableJoystick)joystick).update())
			this.cancel();
	}
	
	
	@Override
	public boolean isFinished() {
		return !((RecordableJoystick)joystick).notDone();
	}
	
	@Override
	public void end() {
		super.end();
		((RecordableJoystick)joystick).end();
	}
}