package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.OI;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ReplayRecordedJoystick extends Command {
	
	private final Supplier<String> nameSupplier;
	
	private Joystick driverStick;
	private Joystick operatorStick;

	public ReplayRecordedJoystick(Joystick driverStick, Joystick operatorStick, String name) {
		this(driverStick, operatorStick, () -> name);
	}
	
	public ReplayRecordedJoystick(Joystick driverStick, Joystick operatorStick, Supplier<String> nameSupplier) {
		
		this.nameSupplier = nameSupplier;
		
		this.driverStick = driverStick;
		this.operatorStick = operatorStick;
	}
	
	@Override
	public void initialize() {
    	String name = nameSupplier.get();
    	String folder = "/home/lvuser/joystick-recordings/" + name + "/";
    	
    	String driverLoc = folder + name + "-driver";
		String operatorLoc = folder + name + "-operator";
		
		driverStick = new RecordableJoystick(driverStick, driverLoc, true);
		operatorStick = new RecordableJoystick(operatorStick, operatorLoc, true);
		
		((RecordableJoystick)driverStick).init(this::timeSinceInitialized);
		((RecordableJoystick)operatorStick).init(this::timeSinceInitialized);
		
		if(Robot.driveTrain.getDefaultCommand() instanceof ArcadeDriveWithJoystick) {
			((ArcadeDriveWithJoystick)Robot.driveTrain.getDefaultCommand()).setJoystick(driverStick);
		}
		
		Robot.oi.destroy();
		Robot.oi = new OI(driverStick, operatorStick);
		
		System.out.println("Replaying joystick from folder " + folder);
	}
	
	@Override
	public void execute() {	
		if(!((RecordableJoystick)driverStick).update() &
				!((RecordableJoystick)operatorStick).update())
			this.cancel();
	}
	
	
	@Override
	public boolean isFinished() {
		return !((RecordableJoystick)driverStick).notDone() |
				!((RecordableJoystick)operatorStick).notDone();
	}
	
	@Override
	public void end() {
		super.end();
		((RecordableJoystick)driverStick).end();
		((RecordableJoystick)operatorStick).end();
		
		Robot.oi.destroy();
		Robot.oi = new OI(driverStick, operatorStick);
	}
}