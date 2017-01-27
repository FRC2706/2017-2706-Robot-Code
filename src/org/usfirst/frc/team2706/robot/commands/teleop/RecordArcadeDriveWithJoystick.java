package org.usfirst.frc.team2706.robot.commands.teleop;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

import edu.wpi.first.wpilibj.Joystick;

public class RecordArcadeDriveWithJoystick extends ArcadeDriveWithJoystick {
	
	private final Supplier<String> nameSupplier;

	public RecordArcadeDriveWithJoystick(Joystick joy, Supplier<String> nameSupplier) {
		super(joy);
		
		this.nameSupplier = nameSupplier;
	}
	
	@Override
	public void initialize() {
    	String name = nameSupplier.get();
    	String folder = "/home/lvuser/joystick-recordings/" + name + "/";
    	
    	String driverLoc = folder + name + "-driver";
    	@SuppressWarnings("unused")
		String operatorLoc = folder + name + "-operator";
		
		joystick = new RecordableJoystick(joystick, driverLoc, false);
		
		System.out.println("Recording joystick to folder " + folder);
		
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