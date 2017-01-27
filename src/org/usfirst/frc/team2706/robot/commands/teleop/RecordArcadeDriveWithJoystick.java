package org.usfirst.frc.team2706.robot.commands.teleop;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.OI;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

import edu.wpi.first.wpilibj.Joystick;

public class RecordArcadeDriveWithJoystick extends ArcadeDriveWithJoystick {
	
	private final Supplier<String> nameSupplier;
	
	private Joystick operatorStick;

	public RecordArcadeDriveWithJoystick(Joystick driverStick, Joystick operatorStick, Supplier<String> nameSupplier) {
		super(driverStick);
		
		this.nameSupplier = nameSupplier;
		
		this.operatorStick = operatorStick;
	}
	
	@Override
	public void initialize() {
    	String name = nameSupplier.get();
    	String folder = "/home/lvuser/joystick-recordings/" + name + "/";
    	
    	String driverLoc = folder + name + "-driver";
		String operatorLoc = folder + name + "-operator";
		
		joystick = new RecordableJoystick(joystick, driverLoc, false);
		operatorStick = new RecordableJoystick(operatorStick, operatorLoc, false);
		Robot.oi = new OI(joystick, operatorStick);
		
		System.out.println("Recording joystick to folder " + folder);
		
		((RecordableJoystick)joystick).init(this::timeSinceInitialized);
	}
	
	@Override
	public void execute() {
		super.execute();
		
		if(!((RecordableJoystick)joystick).update() && !((RecordableJoystick)operatorStick).update() )
			this.cancel();
	}
	
	
	@Override
	public boolean isFinished() {
		return !((RecordableJoystick)joystick).notDone() || !((RecordableJoystick)operatorStick).notDone();
	}
	
	@Override
	public void end() {
		super.end();
		((RecordableJoystick)joystick).end();
		((RecordableJoystick)operatorStick).end();
	}
}