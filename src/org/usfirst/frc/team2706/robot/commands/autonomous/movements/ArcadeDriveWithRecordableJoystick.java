package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.OI;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

import edu.wpi.first.wpilibj.Joystick;

public class ArcadeDriveWithRecordableJoystick extends ArcadeDriveWithJoystick {
	
	private final Supplier<String> nameSupplier;
	
	private Joystick operatorStick;

	public ArcadeDriveWithRecordableJoystick(Joystick driverStick, Joystick operatorStick, Supplier<String> nameSupplier) {
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
		
		joystick = new RecordableJoystick(joystick, driverLoc, true);
		operatorStick = new RecordableJoystick(operatorStick, operatorLoc, true);
		Robot.oi = new OI(joystick, operatorStick);
		
		System.out.println("Recording joystick to folder " + folder);
		
		((RecordableJoystick)joystick).init(this::timeSinceInitialized);
	}
	
	@Override
	public void execute() {
		super.execute();
		
		if(!((RecordableJoystick)joystick).update() && !((RecordableJoystick)operatorStick).update())
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